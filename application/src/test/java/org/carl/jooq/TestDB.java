package org.carl.jooq;

import static org.carl.generated.Tables.DOCUMENTS;
import java.util.List;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import org.carl.auth.utils.EncryptionUtils;
import org.carl.generated.tables.pojos.Documents;
import org.carl.engine.DB;
import org.jooq.CommonTableExpression;
import org.jooq.impl.DSL;
import org.jooq.impl.SQLDataType;
import org.junit.jupiter.api.Test;

@QuarkusTest
public class TestDB {

    @Inject
    DB DB;

    @Test
    public void testGetSql() {
    }

    @Test
    public void testCTE() {
        List<Integer> integers = DB.get(dsl -> {
            CommonTableExpression<?> cte = DSL.name("t").as(DSL.select(DOCUMENTS.ID).from(DOCUMENTS)
                .where(DOCUMENTS.ID.eq(1)).and(DOCUMENTS.IS_ARCHIVE.isFalse())
                .unionAll(DSL.select(DOCUMENTS.ID).from(DOCUMENTS).innerJoin(DSL.table("t"))
                    .on(DSL.field(DSL.name("t", DOCUMENTS.ID.getName()),
                        SQLDataType.INTEGER).eq(DOCUMENTS.PARENT_DOCUMENT_ID))
                    .and(DOCUMENTS.IS_ARCHIVE.isFalse())));
            return dsl.withRecursive(cte).selectFrom(cte).fetchInto(Integer.class);
        });
        System.out.println(integers);
    }

    @Test
    public void testJOOQGet() {
        Documents documents1 = new Documents();
        List<Documents> documents = DB.get(dsl -> dsl.selectFrom(DOCUMENTS)
            .where(DOCUMENTS.PARENT_DOCUMENT_ID.eq(documents1.getId()))
            .fetchInto(Documents.class));
    }

    @Test
    public void testEncrypt() {
        String s = EncryptionUtils.passwordEncoder("1234");
        System.out.println(s);
    }
}
