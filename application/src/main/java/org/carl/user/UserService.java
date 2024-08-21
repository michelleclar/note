package org.carl.user;

import io.quarkus.cache.Cache;
import io.quarkus.cache.CacheName;
import io.quarkus.runtime.StartupEvent;
import io.quarkus.security.UnauthorizedException;
import io.smallrye.jwt.auth.principal.JWTParser;
import io.smallrye.mutiny.Uni;
import io.smallrye.mutiny.unchecked.Unchecked;
import io.vertx.mutiny.core.eventbus.EventBus;
import jakarta.enterprise.event.Observes;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import jakarta.ws.rs.ServerErrorException;
import java.time.Duration;
import org.carl.auth.utils.JwtUtils;
import org.carl.cache.CacheFields;
import org.carl.cache.CacheUtils;
import org.carl.commons.VerifyUtil;
import org.carl.generated.tables.pojos.UserInfo;
import org.carl.generated.tables.pojos.Users;
import org.carl.generated.tables.records.UserOauthRecord;
import org.carl.generated.tables.records.UsersRecord;
import org.carl.engine.DB;
import org.carl.listen.ListenFields;
import org.carl.smtp.SMTPService;
import org.carl.user.exception.UserRegisterException;
import org.carl.user.inter.IUserService;
import org.carl.user.model.Role;
import org.carl.user.model.User;
import org.jboss.logging.Logger;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import static org.carl.generated.Tables.*;

@Singleton
public class UserService implements IUserService {
    private final Logger log = Logger.getLogger(UserService.class);

    @Inject
    EventBus bus;

    @Inject
    SMTPService smtpService;
    Map<String, Integer> roleCache;

    @CacheName(CacheFields.CODE)
    public Cache codeCache;
    @CacheName(CacheFields.USERINFO)
    public Cache userInfoCache;
    @Inject
    JWTParser jwtParser;
    @Inject
    DB DB;

    void onStarted(@Observes StartupEvent startupEvent) {
        initRoles();
    }


    private void initRoles() {
        for (Role role : Role.values()) {
            DB.run(
                dsl -> dsl.insertInto(ROLES, ROLES.NAME).values(role.name()).onDuplicateKeyIgnore()
                    .execute());
        }
    }

    /**
     * Third-party login registration
     * A new refreshToken must be generated regardless of whether the user exists.
     *
     * @param u User information (currently only GitHub login is implemented)
     * @return User information
     */
    public Uni<User> registerByOAuth(User u) {
        return Uni.createFrom().item(u).onItem().transform(Unchecked.function(user -> {
            UserOauthRecord existByOAuth = isExistByOAuth(u);

            if (existByOAuth != null) {
                user.setId(existByOAuth.getUserId());
                String refreshToken = JwtUtils.generateRefreshToken(user.getId());
                DB.run(dsl -> dsl.update(USER_INFO).set(USER_INFO.REFRESH_TOKEN, refreshToken)
                    .where(USER_INFO.USER_ID.eq(existByOAuth.getUserId())).execute());
                return user;
            }

            //FIX: During development, there were instances where executing SQL queries was slow,
            // and investigation is needed.
            DB.transaction(dsl -> {
                UsersRecord usersRecord =
                    dsl.insertInto(USERS, USERS.EMAIL, USERS.USERNAME, USERS.IMAGE_URL)
                        .values(user.getEmail(), user.getUsername(), user.getImageUrl())
                        .onDuplicateKeyIgnore().returning(USERS.ID).fetchOne();
                assert usersRecord != null;
                String refreshToken = JwtUtils.generateRefreshToken(usersRecord.getId());
                dsl.insertInto(USER_INFO, USER_INFO.USER_ID, USER_INFO.REFRESH_TOKEN)
                    .values(usersRecord.getId(), refreshToken)
                    .onDuplicateKeyIgnore().execute();
                dsl.insertInto(USER_OAUTH, USER_OAUTH.USER_ID, USER_OAUTH.PROVIDER_USER_ID,
                        USER_OAUTH.PROVIDER_ID,
                        USER_OAUTH.CREATED_AT, USER_OAUTH.UPDATED_AT, USER_OAUTH.USERINFO,
                        USER_OAUTH.ACCESS_TOKEN)
                    .values(usersRecord.getId(), user.getUserOauth().getProviderUserId(),
                        user.getUserOauth().getProviderId(),
                        user.getUserOauth().getCreatedAt(), user.getUserOauth().getUpdatedAt(),
                        user.getUserOauth().getUserinfo(), user.getUserOauth().getAccessToken())
                    .onDuplicateKeyIgnore().execute();
                Integer roleId = getRoleId(Role.user.name());
                dsl.insertInto(USER_PERMISSIONS).set(USER_PERMISSIONS.USER_ID, usersRecord.getId())
                    .set(USER_PERMISSIONS.ROLE_ID, roleId).execute();
                user.setId(usersRecord.getId());
            });
            return user;
        }));
    }

    public Uni<JwtUtils.JwtPojo> toAuth(String cookieCode) {
        return CacheUtils.getV(userInfoCache, cookieCode, User.class).onItem()
            .transform(Unchecked.function(userinfo -> {
                if (userinfo == null) {
                    log.errorf("serverError,code: %s", cookieCode);
                    throw new ServerErrorException("serverError", 500);
                }
                String accentToken = JwtUtils.generateAccentToken(userinfo);
                String refreshToken = DB.get(dsl ->
                    dsl.select(USER_INFO.REFRESH_TOKEN).from(USER_INFO)
                        .where(USER_INFO.USER_ID.equal(userinfo.getId()))
                        .fetchOneInto(String.class));
                return new JwtUtils.JwtPojo(accentToken, refreshToken);
            })).onFailure().retry().withBackOff(Duration.ofSeconds(3), Duration.ofSeconds(20))
            .atMost(3);
    }

    public Uni<JwtUtils.JwtPojo> refreshToken(String refreshToken, String accessToken) {
        return CacheUtils.getV(userInfoCache, refreshToken, User.class).onItem()
            .transform(Unchecked.function(item -> {
                if (!accessToken.isEmpty()) {
                    return JwtUtils.convexAccentToken(jwtParser, accessToken, refreshToken);
                }

                if (item != null && JwtUtils.verifyToken(jwtParser,
                    item.getUserInfo().getRefreshToken())) {
                    return new JwtUtils.JwtPojo(JwtUtils.generateAccentToken(item));
                }
                UserInfo userInfo = DB.get(dsl ->
                    dsl.selectFrom(USER_INFO).where(USER_INFO.REFRESH_TOKEN.eq(refreshToken))
                        .fetchOneInto(UserInfo.class)
                );
                if (userInfo == null) {
                    throw new UnauthorizedException("Authentication expired.");
                }
                if (!JwtUtils.verifyToken(jwtParser, userInfo.getRefreshToken())) {
                    DB.run(dsl -> {
                        dsl.update(USER_INFO).setNull(USER_INFO.REFRESH_TOKEN)
                            .where(USER_INFO.USER_ID.eq(userInfo.getUserId())).execute();
                    });
                    throw new UnauthorizedException("Authentication expired.");
                }
                User user =
                    DB.get(dsl -> dsl.selectFrom(USERS).where(USERS.ID.eq(userInfo.getUserId()))
                        .fetchOneInto(User.class));
                List<Role> roles = DB.get(
                    dsl -> dsl.select(ROLES.NAME)
                        .from(USER_PERMISSIONS)
                        .join(ROLES).on(ROLES.ID.eq(USER_PERMISSIONS.ROLE_ID))
                        .where(USER_PERMISSIONS.USER_ID.eq(user.getId()))
                        .fetch().into(Role.class));
                user.setRoles(roles);
                JwtUtils.JwtPojo jwtPojo = new JwtUtils.JwtPojo(JwtUtils.generateAccentToken(user));
                user.setUserInfo(userInfo);
                user.setAccessToken(jwtPojo.getAccessToken());
                CacheUtils.putKV(userInfoCache, refreshToken, user);
                return jwtPojo;
            }));
    }


    @Override
    public Uni<Integer> deleteUserById(Integer id) {

        Integer i = DB.get(dsl -> dsl.update(USER_INFO).set(USER_INFO.IS_DELETE, true)
            .where(USER_INFO.USER_ID.equal(id)).and(USER_INFO.IS_DELETE.isFalse()).execute());

        bus.send(ListenFields.USER, id);
        return Uni.createFrom().item(i);
    }

    @Override
    public Uni<List<Users>> findAll() {
        List<Users> r = DB.get(
            dsl -> dsl.select(USERS.ID, USERS.USERNAME, USERS.EMAIL).from(USERS)
                .fetchInto(Users.class));
        return Uni.createFrom().item(r);
    }


    public Object[] genCode(String email) {
        Object[] image = VerifyUtil.newBuilder().build().createImage();
        CacheUtils.putKV(codeCache, email, image[0]);
        return image;
    }

    @Override
    public Uni<User> login(User u) {
        return Uni.createFrom().item(u).onItem().transform(Unchecked.function(item -> new User()));
    }

    private boolean check(String code, String email) {
        String _code =
            CacheUtils.getV(codeCache, email, String.class).await().indefinitely();
        if (_code.isBlank()) {
            return false;
        }
        return _code.equals(code);
    }

    @Override
    public Uni<User> register(User u) {
        return Uni.createFrom().item(u).onItem().transform(Unchecked.function(user -> {
            if (!user.checkMail()) {
                throw new UserRegisterException("Email format is incorrect.");
            }
            if (Objects.isNull(user.getCode()) || !check(user.getCode(), user.getEmail())) {
                throw new UserRegisterException("Code error.");
            }

            user.insert();
            return user;
        }));
    }

    public UserOauthRecord isExistByOAuth(User u) {

        return DB.get(dsl -> dsl.selectFrom(USER_OAUTH)
            .where(USER_OAUTH.PROVIDER_USER_ID.eq(u.getUserOauth().getProviderUserId()))
            .and(USER_OAUTH.PROVIDER_ID.eq(u.getUserOauth().getProviderId()))
            .fetchOne()
        );
    }


    @Override
    public Uni<Boolean> sendCodeToEmail(String email) {
        return Uni.createFrom().item(email).onItem().transform(Unchecked.function(mail -> {
            if (!User.checkMail(mail)) {
                throw new UserRegisterException("Email format is incorrect.");
            }
            Object[] genCode = genCode(email);
            smtpService.send(email, (String) genCode[0]).subscribe().with(i -> {

            });
            return true;
        }));
    }

    @Override
    public Uni<Boolean> isExistByEmail(String email) {
        return null;
    }


    public Users getUserInfoById(Integer id) {
        return DB.get(
            dsl -> dsl.select(USERS.ID, USERS.USERNAME, USERS.EMAIL, USERS.IMAGE_URL).from(USERS)
                .where(USERS.ID.eq(id)).fetchOneInto(Users.class));
    }

    void setRoleCache() {
        roleCache = new HashMap<>();
        roleCache = DB.get(
            dsl -> dsl.select(ROLES.ID, ROLES.NAME).from(ROLES).fetchMap(ROLES.NAME, ROLES.ID));
    }

    Integer getRoleId(String name) {
        if (roleCache == null) {
            setRoleCache();
        }
        return roleCache.get(name);
    }


}