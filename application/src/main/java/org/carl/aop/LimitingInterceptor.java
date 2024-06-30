package org.carl.aop;

import io.quarkus.cache.Cache;
import io.quarkus.cache.CacheName;
import io.quarkus.cache.CaffeineCache;
import io.smallrye.mutiny.Uni;
import io.vertx.core.http.HttpServerRequest;
import jakarta.annotation.Priority;
import jakarta.inject.Inject;
import jakarta.interceptor.AroundInvoke;
import jakarta.interceptor.Interceptor;
import jakarta.interceptor.InvocationContext;
import jakarta.ws.rs.core.Response;

import java.util.Objects;
import java.util.concurrent.CompletableFuture;

import org.carl.aop.annotate.Limiting;
import org.carl.cache.CacheFields;
import org.carl.commons.AppStatus;
import org.jboss.logging.Logger;

@Interceptor
@Limiting
@Priority(1000)
public class LimitingInterceptor {
    private final Logger log = Logger.getLogger(LimitingInterceptor.class);
    @Inject
    HttpServerRequest request;

    @CacheName(CacheFields.IP)
    Cache cache;

    @AroundInvoke
    Object limitingInvocation(InvocationContext ctx) {
        String host = request.remoteAddress().host();
        if (!"127.0.0.1".equals(host)) {
            CompletableFuture<Object> ifPresent =
                    cache.as(CaffeineCache.class).getIfPresent(host);
            if (!Objects.isNull(ifPresent)) {
                return Uni.createFrom().item(Response.status(AppStatus.USER_EMAIL_EXISTS).build());
            }
            cache.as(CaffeineCache.class).put(host,
                    CompletableFuture.completedFuture(true));
        }

        try {
            return ctx.proceed();
        } catch (Exception e) {
            log.error(e.getMessage(),e);
            return Uni.createFrom().item(Response.status(AppStatus.USER_EMAIL_EXISTS).build());
        }
    }


}
