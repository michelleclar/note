package org.carl;

import jakarta.mail.*;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import java.util.Properties;
public class SmtpService {
    String account;
    String password;
    Properties props;

    public SmtpService(String account, String password) {
        this.account = account;
        this.password = password;
        props = System.getProperties();

        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.ssl.enable", "true");
        props.put("mail.smtp.host", "smtp.qq.com");
        props.put("mail.user", account);
        props.put("mail.password", password);
        props.put("mail.smtp.socketFactory.port", "587"); // win 25 unix 587
        props.put("mail.smtp.starttls.enable", "true");
    }

    public boolean sendEmail(String to, String content) {
        Session session = getSession();
        session.setDebug(true);

        MimeMessage message = new MimeMessage(session);
        try {
            message.setFrom(new InternetAddress(account));
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(to));
            message.setText("This is actual message");
            message.setSubject("This is the Subject Line!");
            Transport.send(message);
        } catch (MessagingException e) {

            return false;
        }
        return true;
    }

    Session getSession() {

        return Session.getDefaultInstance(
            props,
            new Authenticator() {
                public PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(account, password);
                }
            });
    }
}