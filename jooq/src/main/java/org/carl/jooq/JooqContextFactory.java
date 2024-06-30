package org.carl.jooq;

import static io.agroal.api.configuration.AgroalConnectionFactoryConfiguration.TransactionIsolation.SERIALIZABLE;
import static io.agroal.api.configuration.AgroalConnectionPoolConfiguration.ConnectionValidator.defaultValidator;

import io.agroal.api.AgroalDataSource;
import io.agroal.api.configuration.AgroalDataSourceConfiguration;
import io.agroal.api.configuration.supplier.AgroalDataSourceConfigurationSupplier;
import io.agroal.api.security.NamePrincipal;
import io.agroal.api.security.SimplePassword;
import java.sql.SQLException;
import java.time.Duration;
import org.eclipse.microprofile.config.ConfigProvider;
import org.jooq.DSLContext;
import org.jooq.SQLDialect;
import org.jooq.impl.DSL;

public class JooqContextFactory {

    String jdbcUrl =
        ConfigProvider.getConfig().getValue("quarkus.datasource.jdbc.url", String.class);

    String username =
        ConfigProvider.getConfig().getValue("quarkus.datasource.username", String.class);

    String password =
        ConfigProvider.getConfig().getValue("quarkus.datasource.password", String.class);


    public AgroalDataSource initializeDataSource() throws SQLException {
        AgroalDataSourceConfigurationSupplier configuration =
            new AgroalDataSourceConfigurationSupplier()
                .dataSourceImplementation(
                    AgroalDataSourceConfiguration.DataSourceImplementation.AGROAL) // Set data
                // source implementation to Agroal
                .metricsEnabled(false) // Disable metrics
                // Configure connection pool
                .connectionPoolConfiguration(cp -> cp
                    // Set minimum pool size
                    .minSize(5)
                    // Set maximum pool size
                    .maxSize(20)
                    // Set initial pool size
                    .initialSize(10)
                    // Set connection validator
                    .connectionValidator(defaultValidator())
                    // Set acquisition timeout for obtaining connections
                    .acquisitionTimeout(Duration.ofSeconds(5))
                    // Set leak detection timeout
                    .leakTimeout(Duration.ofSeconds(5))
                    // Set validation timeout
                    .validationTimeout(Duration.ofSeconds(50))
                    // Set reap timeout for reclaiming connections
                    .reapTimeout(Duration.ofSeconds(500))
                    // Configure connection factory
                    .connectionFactoryConfiguration(cf -> cf
                        // Set JDBC URL
                        .jdbcUrl(jdbcUrl)
                        // Set connection provider class name
                        .connectionProviderClassName("org.postgresql.Driver")
                        // Set auto-commit to true
                        .autoCommit(true)
                        // Set transaction isolation level to SERIALIZABLE
                        .jdbcTransactionIsolation(SERIALIZABLE)
                        // Set username
                        .principal(new NamePrincipal(username))
                        // Set password
                        .credential(new SimplePassword(password))
                    )
                );
        return AgroalDataSource.from(configuration);
    }

    public JooqContext createJooqContext(ClientContext clientContext) {
        try {

            DSLContext ctx = DSL.using(initializeDataSource(), SQLDialect.POSTGRES);
            return new JooqContext(clientContext, ctx);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}