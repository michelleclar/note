package org.carl.cache;

import io.quarkus.cache.Cache;
import io.quarkus.cache.CacheName;
import io.quarkus.cache.CaffeineCache;
import io.quarkus.test.junit.QuarkusTest;
import java.util.concurrent.ExecutionException;
import org.carl.user.model.User;
import org.junit.jupiter.api.Test;

@QuarkusTest
public class TestCache {
    @CacheName(CacheFields.CODE)
    public Cache codeCache;
    @Test
    public void testGet() throws ExecutionException, InterruptedException {
        User user = new User();
        user.setId(2);
        user.setEmail("22231");
        CacheUtils.putKV(codeCache, "code", user);
        Object code = codeCache.as(CaffeineCache.class).getIfPresent("code").get();
        CacheUtils.getV(codeCache, "code", User.class).subscribe().with(u -> {
            System.out.println(u.getEmail());
        });
        System.out.println(code);
    }
}
