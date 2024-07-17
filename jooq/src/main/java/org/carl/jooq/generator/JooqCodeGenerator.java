package org.carl.jooq.generator;

import org.eclipse.microprofile.config.ConfigProvider;
import org.jooq.codegen.GenerationTool;
import org.jooq.meta.jaxb.Configuration;
import org.jooq.meta.jaxb.Database;
import org.jooq.meta.jaxb.Generate;
import org.jooq.meta.jaxb.Generator;
import org.jooq.meta.jaxb.Jdbc;
import org.jooq.meta.jaxb.Target;

public class JooqCodeGenerator {

    String jdbcUrl =
            ConfigProvider.getConfig().getValue("quarkus.datasource.jdbc.url", String.class);

    String username =
            ConfigProvider.getConfig().getValue("quarkus.datasource.username", String.class);

    String password =
            ConfigProvider.getConfig().getValue("quarkus.datasource.password", String.class);

    public void codeGenByContainers() throws Exception {

        // Generate JOOQ code programmatically
        Configuration configuration =
                new Configuration()
                        .withJdbc(
                                new Jdbc()
                                        .withDriver("org.postgresql.Driver")
                                        .withUrl(jdbcUrl)
                                        .withUser(username)
                                        .withPassword(password))
                        .withGenerator(
                                new Generator()
                                        .withDatabase(
                                                new Database()
                                                        .withName("org.jooq.meta.postgres.PostgresDatabase")
                                                        .withInputSchema("public")
                                                        .withIncludes(".*")
                                                        .withExcludes(""))
                                        .withGenerate(
                                                new Generate()
                                                        .withPojos(true)
                                                        .withInterfaces(true)
                                                        .withDaos(true))
                                        .withTarget(
                                                new Target()
                                                        .withPackageName("org.carl.generated")
                                                        .withDirectory("../application/src/main/generated")));

        GenerationTool.generate(configuration);
    }

    public static void main(String[] args) throws Exception {
        JooqCodeGenerator jooqCodeGenerator = new JooqCodeGenerator();
        jooqCodeGenerator.codeGenByContainers();
    }
}
