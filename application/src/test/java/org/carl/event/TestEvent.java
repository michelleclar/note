package org.carl.event;

import io.quarkus.test.junit.QuarkusTest;
import io.vertx.mutiny.core.eventbus.EventBus;
import jakarta.inject.Inject;
import org.carl.listen.ListenFields;
import org.carl.user.model.User;
import org.junit.jupiter.api.Test;

@QuarkusTest
public class TestEvent {
    @Inject
    EventBus bus;

    @Test
    public void testAwait() {
        bus.request(ListenFields.USER, 1).subscribe().with(message -> {
            User body = (User) message.body();

            System.out.println(body.getUuid());
        });
    }
}