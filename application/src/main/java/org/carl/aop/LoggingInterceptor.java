package org.carl.aop;

import jakarta.annotation.Priority;
import jakarta.inject.Inject;
import jakarta.interceptor.AroundInvoke;
import jakarta.interceptor.Interceptor;
import jakarta.interceptor.InvocationContext;
import org.carl.aop.annotate.Logged;
import org.jboss.logging.Logger;

@Logged
@Priority(2020)
@Interceptor
public class LoggingInterceptor {

    @Inject
    Logger logger;

    @AroundInvoke
    Object logInvocation(InvocationContext context) throws Exception {
        logger.debugf("Method:%s", context.getMethod().getName());
        logger.debugf("Params:%s",context.getParameters());
        return context.proceed();
    }

}