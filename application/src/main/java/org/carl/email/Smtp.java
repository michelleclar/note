package org.carl.email;

import org.carl.SmtpService;
import org.eclipse.microprofile.config.ConfigProvider;

public class Smtp {
    static final SmtpService smtp;

    static {
        String password = ConfigProvider.getConfig().getValue("email.password", String.class);
        String account = ConfigProvider.getConfig().getValue("email.account", String.class);
        smtp = new SmtpService(account, password);
    }

    public static boolean sendMail(String to, String context){
        if ("dev".equals(ConfigProvider.getConfig().getValue("quarkus.profile", String.class))){
            System.out.println(context);
            return true;
        }

        return smtp.sendEmail(to, context);
    }
}