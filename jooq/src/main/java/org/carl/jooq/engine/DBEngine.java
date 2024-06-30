package org.carl.jooq.engine;

import java.util.function.Function;
import org.jooq.DSLContext;
import org.jooq.InsertResultStep;
import org.jooq.Record;
import org.jooq.SQLDialect;
import org.jooq.SelectWhereStep;
import org.jooq.impl.DSL;

public class DBEngine {
    static DSLContext dsl = DSL.using(SQLDialect.POSTGRES);

    public static <R extends Record> String getSqlByJooq(
        Function<DSLContext, SelectWhereStep<R>> query) {
        return query.apply(dsl).getSQL();
    }

    public static String _getSqlByJooq(
        Function<DSLContext, InsertResultStep<Record>> query) {

        return query.apply(dsl).getSQL();
    }
}