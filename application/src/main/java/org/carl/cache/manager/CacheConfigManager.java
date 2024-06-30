package org.carl.cache.manager;

import io.quarkus.cache.Cache;
import io.quarkus.cache.CacheManager;
import io.quarkus.cache.CaffeineCache;
import jakarta.inject.Singleton;

import java.time.Duration;
import java.util.Optional;

@Singleton
public class CacheConfigManager {
  private final CacheManager cacheManager;

  public CacheConfigManager(CacheManager cacheManager) {
    this.cacheManager = cacheManager;
  }



  /**
   * 获取cache
   *
   * @param cacheName 缓存名称
   * @return cache
   */

  public Optional<Cache> getCache(String cacheName) {
    return cacheManager.getCache(cacheName);
  }

  /**
   * 设置缓存过期时间
   *
   * @param cacheName 缓存名称
   * @param duration 过期时间
   */
  public void setExpireAfterAccess(String cacheName, Duration duration) {
    cacheManager.getCache(cacheName)
        .ifPresent(value -> value.as(CaffeineCache.class).setExpireAfterAccess(duration));
  }

  /**
   * 设置缓存写入后过期时间
   *
   * @param cacheName 缓存名称
   * @param duration 过期时间
   */
  public void setExpireAfterWrite(String cacheName, Duration duration) {
    cacheManager.getCache(cacheName)
        .ifPresent(value -> value.as(CaffeineCache.class).setExpireAfterWrite(duration));
  }

  /**
   * 设置缓存最大容量
   *
   * @param cacheName 缓存名称
   * @param maximumSize 最大容量
   */
  public void setMaximumSize(String cacheName, long maximumSize) {

    cacheManager.getCache(cacheName)
        .ifPresent(value -> value.as(CaffeineCache.class).setMaximumSize(maximumSize));
  }

}
