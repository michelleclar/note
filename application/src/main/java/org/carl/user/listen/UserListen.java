package org.carl.user.listen;

import io.quarkus.vertx.ConsumeEvent;
import io.vertx.core.json.JsonObject;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import org.carl.aop.annotate.Logged;
import org.carl.cache.CacheUtils;
import org.carl.listen.ListenFields;
import org.carl.user.UserService;
import org.carl.user.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;

@Logged
@ApplicationScoped
public class UserListen {
    private static final Logger log = LoggerFactory.getLogger(UserListen.class);
    @Inject
    UserService userService;

    @ConsumeEvent(ListenFields.USER_REGISTER)
    public Integer userRegister(JsonObject json) {
        try {
            User user = User.of(json);
            AtomicInteger id = new AtomicInteger();
            userService.registerByOAuth(user).onFailure().retry()
                .withBackOff(Duration.ofSeconds(3), Duration.ofSeconds(20)).atMost(3).subscribe()
                .with(u -> {
                    id.set(u.getId());
                    CacheUtils.putKV(userService.userInfoCache, u.getUuid(), u);
                });

            return id.get();
        } catch (Exception e) {
            log.error("⛵⛵⛵ Sync db user error on insert db:{}", e.getMessage(), e);
            throw new RuntimeException(e);
        }

    }


    @ConsumeEvent(ListenFields.USER)
    public User greeting(Integer id) {
        User user = new User();
        user.setUuid(UUID.randomUUID().toString());
        return user;
    }

    @ConsumeEvent(ListenFields.CLEAN_CACHE_CODE)
    public void cleanCacheCode(String email) {
        CacheUtils.invalidateCache(userService.codeCache, email);
    }
}