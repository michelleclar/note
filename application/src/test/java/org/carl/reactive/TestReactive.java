package org.carl.reactive;

import io.smallrye.mutiny.Uni;

public class TestReactive {
    // @Test
    public void testUni() {
        String str = "Hello";
        Uni<String> uni = Uni.createFrom().item(str).onItem()
            .transform(item -> getWord().onItem().transform(word -> {
                System.out.println('a');
                return item + " " + word;
            }).await().indefinitely());
        System.out.println('b');
        uni.subscribe().with(System.out::println);
    }
    Uni<String> getWord() {
        return Uni.createFrom().item("word");
    }
}
