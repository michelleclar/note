package org.carl.jooq;

import org.eclipse.microprofile.config.ConfigProvider;

final public class ClientContext {

    public final static int clientId = ConfigProvider.getConfig().getValue("app.id", int.class);;

    public int getClientId() {
        return clientId;
    }

}
