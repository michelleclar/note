package org.carl.jooq.engine;

import org.carl.jooq.ClientContext;
import org.carl.jooq.JooqContext;
import org.carl.jooq.JooqContextFactory;
import org.jboss.logging.Logger;
import org.jooq.*;
import org.jooq.Record;
import org.jooq.impl.DSL;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * JOOQ
 * For a single data source scenario, it will automatically inject the default data source and
 * manage the data source connections.
 */
public class DB {
    private static final Logger log = Logger.getLogger(DB.class);

    static JooqContext ctx;

    static {
        JooqContextFactory jooqContextFactory = new JooqContextFactory();
        ctx = jooqContextFactory.createJooqContext(new ClientContext());
    }

    public static <T> T get(Function<DSLContext, T> queryFunction) {
        return queryFunction.apply(ctx.getCtx());
    }

    public static void run(Consumer<DSLContext> queryFunction) {
        queryFunction.accept(ctx.getCtx());
    }

    static Map<String, Object> dtoFactory = new HashMap<>();

    @SuppressWarnings("unchecked")
    public static <T> T dto(Function<Configuration, T> function) {
        Configuration conf = ctx.getCtx().configuration();
        String key = function.getClass().getSimpleName().split("[$]")[0];
        Object o = dtoFactory.get(key);
        if (o == null) {
            dtoFactory.put(key, function.apply(conf));
        }

        return (T) dtoFactory.get(key);
    }

    public static void transaction(Consumer<DSLContext> queryFunction) {
        long start = System.currentTimeMillis();
        log.debugf("Transaction execution start time :%s", start);
        ctx.getCtx().transaction(configuration -> {
            DSLContext dsl = DSL.using(configuration);
            queryFunction.accept(dsl);
        });
        log.debugf("Transaction duration: %s", System.currentTimeMillis() - start);
    }

    public static int getAppid() {
        return ctx.getAppId();
    }

    public static <R extends Record> R getRecord(Table<R> table, Object o) {
        return ctx.getCtx().newRecord(table, o);
    }
}