# JOOQ
> This is a record of some changes when using Jooq together with Quarkus.

## Code Generated
> have two method (only gradle)
> gradle plugins
  > 1. use Jooq provide plugins (It seems that this approach isn't very efficient, so we're going to use the following method instead)
  > 2. use Third-party (Currently using this way. link: https://github.com/etiennestuder/gradle-jooq-plugin)
> code


#### use Jooq provide
```groovy
plugins {
    id 'idea'
    id("org.jooq.jooq-codegen-gradle") version "3.19.10"
}

dependencies {
    jooqCodegen 'org.postgresql:postgresql:42.7.3'
}

test {
    useJUnitPlatform()
}

def mySrcDir = 'src/main/java'
def generatedDir = 'src/main/generated'
sourceSets {
    main {
        java {
            srcDir generatedDir
            srcDir mySrcDir
        }
    }
}
idea {
    module {
        generatedSourceDirs.addAll(file(generatedDir))
        sourceDirs.addAll(file(generatedDir))
        sourceDirs.addAll(file(mySrcDir))
    }
}

jooq {
    configuration {
        jdbc {
            url = "jdbc:postgresql://localhost:5432/db"
            driver = "org.postgresql.Driver"
            user = "root"
            password = "root"
        }
        generator {

            // Optional: The jooq-meta configuration, configuring the information schema source.
            database {
                inputSchema = 'public'
            }

            // Optional: The jooq-codegen configuration, configuring the generated output content.
            generate {
                pojos = true
                daos = true
            }

            // Optional: The generation output target
            target {
                packageName = "org.carl.generated"
                directory = "src/main/generated"
            }
        }
    }
}
```

### use Third-party
```groovy
plugins {
    id 'idea'
    id 'nu.studer.jooq' version '9.0'
}

dependencies {
    implementation libs.jooq
    jooqGenerator 'org.postgresql:postgresql:42.7.3'
}

test {
    useJUnitPlatform()
}


def mySrcDir = 'src/main/java'
def generatedDir = 'src/main/generated'
sourceSets {
    main {
        java {
            srcDir generatedDir
            srcDir mySrcDir
        }
    }
}
idea {
    module {
        generatedSourceDirs.addAll(file(generatedDir))
        sourceDirs.addAll(file(generatedDir))
        sourceDirs.addAll(file(mySrcDir))
    }
}


test {
    systemProperty 'java.util.logging.manager', 'org.jboss.logmanager.LogManager'
}
import nu.studer.gradle.jooq.JooqEdition

jooq {
    version = '3.18.7'
    edition = JooqEdition.OSS

    configurations {
        main {
            generationTool {
                logging = org.jooq.meta.jaxb.Logging.WARN
                jdbc {
                    driver = 'org.postgresql.Driver'
                    url = 'jdbc:postgresql://localhost:5432/db'
                    user = 'root'
                    password = 'root'
                    properties {
                        property {
                            key = 'PAGE_SIZE'
                            value = 2048
                        }
                    }
                }
                generator {
                    name = 'org.jooq.codegen.DefaultGenerator'
                    database {
//                        name = 'org.jooq.meta.h2.H2Database'
                        inputSchema = 'public'
//                        forcedTypes {
//                            forcedType {
//                                name = 'varchar'
//                                includeExpression = '.*'
//                                includeTypes = 'JSONB?'
//                            }
//                            forcedType {
//                                name = 'varchar'
//                                includeExpression = '.*'
//                                includeTypes = 'INET'
//                            }
//                        }
                    }
                    generate {
                        deprecated = false
                        records = false
                        immutablePojos = false
                        fluentSetters = false
                        pojos = true
                        daos = true
                    }
                    target {
                        packageName = 'org.carl.generated'
                        directory = 'src/main/generated'
                    }
                    strategy.name = "org.jooq.codegen.DefaultGeneratorStrategy"
                }
            }
        }
    }
}
```

### code

```groovy
plugins {
    id 'java'
    id 'idea'
    id 'org.kordamp.gradle.jandex'
}
dependencies {
    implementation libs.jooq
    implementation libs.quarkusJdbcPostgreSQL
    implementation libs.agroal
    implementation libs.bundles.jooqGen
}

task generateJooqCode(type: JavaExec) {
    group = 'application'
    description = 'Runs JooqCodeGenerator Java Application'
    mainClass = 'org.carl.jooq.generator.JooqCodeGenerator'
    classpath = sourceSets.main.runtimeClasspath
    dependsOn("jandex")
}
```
```java
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

```