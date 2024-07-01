package org.carl;

import org.carl.jooq.generator.JooqCodeGenerator;
import org.junit.jupiter.api.Test;

public class TestJooq {
    @Test
    public void testGen() throws Exception {
        JooqCodeGenerator jooqCodeGenerator = new JooqCodeGenerator();
        jooqCodeGenerator.codeGenByContainers();
    }
}