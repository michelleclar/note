package org.carl.engine;

import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Function;
import org.jboss.logging.Logger;
import org.jooq.Configuration;
import org.jooq.DSLContext;
import org.jooq.Record;
import org.jooq.Table;
import org.jooq.impl.DSL;

/**
 * JOOQ
 * For a single data source scenario, it will automatically inject the default data source and
 * manage the data source connections.
 */
@Singleton
public class DB {
    private final Logger log = Logger.getLogger(DB.class);

    @Inject
    DSLContext dsl;

    public <T> T get(Function<DSLContext, T> queryFunction) {
        return queryFunction.apply(dsl);
    }

    public void run(Consumer<DSLContext> queryFunction) {
        queryFunction.accept(dsl);
    }

    static Map<String, Object> dtoFactory = new HashMap<>();

    @SuppressWarnings("unchecked")
    public <T> T dto(Function<Configuration, T> function) {
        Configuration conf = dsl.configuration();
        String key = function.getClass().getSimpleName().split("[$]")[0];
        Object o = dtoFactory.get(key);
        if (o == null) {
            dtoFactory.put(key, function.apply(conf));
        }

        return (T) dtoFactory.get(key);
    }

    public void transaction(Consumer<DSLContext> queryFunction) {
        long start = System.currentTimeMillis();
        log.debugf("Transaction execution start time :%s", start);
        dsl.transaction(configuration -> {
            DSLContext dsl = DSL.using(configuration);
            queryFunction.accept(dsl);
        });
        log.debugf("Transaction duration: %s", System.currentTimeMillis() - start);
    }


    public <R extends Record> R getRecord(Table<R> table, Object o) {
        return dsl.newRecord(table, o);
    }
}