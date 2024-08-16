package org.carl.user.model;

import io.vertx.core.json.JsonObject;
import java.util.ArrayList;
import java.util.List;
import org.carl.commons.Fields;
import org.carl.generated.tables.pojos.OauthProviders;
import org.carl.generated.tables.pojos.UserInfo;
import org.carl.generated.tables.pojos.UserOauth;
import org.jooq.JSONB;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class User extends org.carl.generated.tables.pojos.Users {

    UserInfo userInfo;
    UserOauth userOauth;
    OauthProviders oauthProviders;
    String code;
    String password;
    List<Role> Roles;
    String uuid;
    String accessToken;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public static boolean checkMail(String mail) {
        Pattern p = Pattern.compile("^[a-zA-Z0-9_-]+@[a-zA-Z0-9_-]+(\\.[a-zA-Z0-9_-]+)+$");
        Matcher m = p.matcher(mail); // 验证内容是否合法
        return m.matches();
    }

    public boolean checkMail() {
        return User.checkMail(this.getEmail());
    }

    public void insert() {
//        Integer id = JOOQ.get(dsl -> Objects.requireNonNull(dsl.insertInto(USER, USER.ROLES,
//        USER.PASSWORD_HASH, USER.EMAIL, USER.USERNAME, USER.ORIGINAL_USERNAME, USER.APP_ID)
//        .values(this.getRoles(), this.getPasswordHash(), this.getEmail(), this.getUsername(),
//        this.getOriginalUsername(), this.getAppId()).onDuplicateKeyIgnore().returning(USER.ID)
//        .fetchOne()).getId());

//        if (id != null) this.setId(id);
    }

    public UserInfo getUserInfo() {
        return userInfo;
    }

    public void setUserInfo(UserInfo userInfo) {
        this.userInfo = userInfo;
    }

    public UserOauth getUserOauth() {
        return userOauth;
    }

    public void setUserOauth(UserOauth userOauth) {
        this.userOauth = userOauth;
    }


    public List<Role> getRoles() {
        return Roles;
    }

    public void setRoles(List<Role> roles) {
        Roles = roles;
    }

    public OauthProviders getOauthProviders() {
        return oauthProviders;
    }

    public void setOauthProviders(OauthProviders oauthProviders) {
        this.oauthProviders = oauthProviders;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public static User of(JsonObject json) {
        User u = new User();
        u.setEmail(json.getString(Fields.EMAIL));
        u.setCode(json.getString(Fields.CODE));
        u.setUsername(json.getString(Fields.NAME));
        u.setImageUrl(json.getString(Fields.AVATAR_URL));
        u.setUuid(json.getString(Fields.UUID));

        UserInfo userinfo = new UserInfo();
        userinfo.setUserId(u.getId());
        u.setUserInfo(userinfo);


        UserOauth userOauth = new UserOauth();
        userOauth.setAccessToken(json.getString(Fields.ACCESS_TOKEN));
        userOauth.setProviderId(json.getInteger(Fields.PROVIDER_ID));
        userOauth.setProviderUserId(json.getInteger(Fields.USER_ID));
        userOauth.setCreatedAt(LocalDateTime.parse(json.getString(Fields.CREATED_AT),
            DateTimeFormatter.ISO_OFFSET_DATE_TIME));
        userOauth.setUpdatedAt(LocalDateTime.parse(json.getString(Fields.UPDATED_AT),
            DateTimeFormatter.ISO_OFFSET_DATE_TIME));
        userOauth.setUserinfo(JSONB.jsonb(json.toString()));
        u.setUserOauth(userOauth);
        u.Roles = new ArrayList<>() {{
            add(Role.user);
        }};

        return u;
    }


    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }
}
