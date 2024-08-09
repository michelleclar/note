package org.carl.jooq.engine;

import java.util.function.Function;
import org.jooq.CloseableQuery;
import org.jooq.DSLContext;
import org.jooq.SQLDialect;
import org.jooq.impl.DSL;

public class DBEngine {
    static DSLContext dsl = DSL.using(SQLDialect.POSTGRES);

    public static String getSqlByJooq(
        Function<DSLContext, CloseableQuery> query) {
        return query.apply(dsl).getSQL();
    }

}