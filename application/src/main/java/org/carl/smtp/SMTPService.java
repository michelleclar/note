package org.carl.smtp;

import io.quarkus.mailer.Mail;
import io.quarkus.mailer.reactive.ReactiveMailer;
import io.smallrye.mutiny.Uni;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;



@Singleton
public class SMTPService {
    @Inject
    ReactiveMailer reactiveMailer;

    public Uni<Void> send(@Valid @Email String to, @Valid @NotBlank String context) {
        return reactiveMailer.send(Mail.withText(to, "valid code from note", context));
    }

}
