package org.carl.jooq;


import org.jooq.DSLContext;

public class JooqContext {

    private final ClientContext clientContext;

    private final DSLContext ctx;

    public JooqContext(ClientContext requestContext, DSLContext ctx) {
        this.clientContext = requestContext;
        this.ctx = ctx;
    }

    public ClientContext getClientContext() {
        return clientContext;
    }

    public int getAppId() {
        return clientContext.getClientId();
    }

    public DSLContext getCtx() {
        return ctx;
    }
}
