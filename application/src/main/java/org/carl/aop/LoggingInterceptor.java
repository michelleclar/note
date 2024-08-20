package org.carl.aop;

import jakarta.annotation.Priority;
import jakarta.inject.Inject;
import jakarta.interceptor.AroundInvoke;
import jakarta.interceptor.Interceptor;
import jakarta.interceptor.InvocationContext;
import org.carl.aop.annotate.Logged;
import org.carl.utils.LogUtil;
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
        String s = LogUtil.beanToString(context.getParameters());
        logger.debugf("Params:%s",s);
        Object ret = context.proceed();
        logger.debugf("Return:%s", ret);
        return ret;
    }

}