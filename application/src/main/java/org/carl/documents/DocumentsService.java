package org.carl.documents;

import static org.carl.generated.Tables.DOCUMENTS;
import static org.carl.generated.Tables.USER_DOCUMENTS;

import io.smallrye.mutiny.Uni;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import org.carl.documents.model.Document;
import org.carl.generated.tables.records.DocumentsRecord;
import org.carl.engine.DB;
import org.jboss.logging.Logger;
import org.jooq.CommonTableExpression;
import org.jooq.Condition;
import org.jooq.UpdateSetFirstStep;
import org.jooq.exception.DataAccessException;
import org.jooq.impl.DSL;
import org.jooq.impl.SQLDataType;
import org.jooq.tools.StringUtils;

@Singleton
public class DocumentsService {
    private final Logger log = Logger.getLogger(DocumentsService.class);
    @Inject
    DB DB;

    public Uni<Integer> remove(Document doc) {
        return Uni.createFrom()
            .item(doc)
            .onItem()
            .transform(
                item -> {
                    checkIsExist(doc);
                    List<Integer> ids =
                        DB.get(
                            dsl -> {
                                CommonTableExpression<?> cte =
                                    DSL.name("t")
                                        .as(
                                            DSL.select(DOCUMENTS.ID)
                                                .from(DOCUMENTS)
                                                .where(DOCUMENTS.ID.eq(doc.getId()))
                                                .and(DOCUMENTS.IS_ARCHIVE.isTrue())
                                                .unionAll(
                                                    DSL.select(DOCUMENTS.ID)
                                                        .from(DOCUMENTS)
                                                        .innerJoin(DSL.table("t"))
                                                        .on(
                                                            DSL.field(
                                                                    DSL.name("t",
                                                                        DOCUMENTS.ID.getName()),
                                                                    SQLDataType.INTEGER)
                                                                .eq(DOCUMENTS.PARENT_DOCUMENT_ID))
                                                        .and(DOCUMENTS.IS_ARCHIVE.isTrue())));
                                return dsl.withRecursive(cte).selectFrom(cte)
                                    .fetchInto(Integer.class);
                            });

                    // TODO: change document isArchive ,need change all children
                    DB.transaction(
                        dsl -> {
                            // NOTE all children
                            dsl.delete(DOCUMENTS).where(DOCUMENTS.ID.in(ids)).execute();
                            dsl.delete(USER_DOCUMENTS)
                                .where(USER_DOCUMENTS.USER_ID.eq(doc.getUserId()))
                                .and(USER_DOCUMENTS.DOCUMENT_ID.in(ids))
                                .execute();
                        });
                    log.infof("delete documents,Id:%d,all childrenIds:%s", doc.getId(), ids);

                    return ids.size();
                });
    }

    public Uni<Integer> removeIcon(Document doc) {
        return Uni.createFrom()
            .item(doc)
            .onItem()
            .transform(
                item ->
                    DB.get(
                        dsl -> {
                            checkIsExist(doc);
                            return dsl.update(DOCUMENTS)
                                .setNull(DOCUMENTS.ICON)
                                .set(DOCUMENTS.UPDATED_AT, LocalDateTime.now())
                                .where(DOCUMENTS.ID.eq(doc.getId()))
                                .execute();
                        }));
    }

    public Uni<Integer> removeCoverImage(Document doc) {
        return Uni.createFrom()
            .item(doc)
            .onItem()
            .transform(
                item ->
                    DB.get(
                        dsl -> {
                            checkIsExist(doc);
                            return dsl.update(DOCUMENTS)
                                .setNull(DOCUMENTS.COVER_IMAGE)
                                .set(DOCUMENTS.UPDATED_AT, LocalDateTime.now())
                                .where(DOCUMENTS.ID.eq(doc.getId()))
                                .execute();
                        }));
    }

    private void checkIsExist(Document doc) {
        Integer id =
            DB.get(
                dsl ->
                    dsl.select(DOCUMENTS.ID)
                        .from(DOCUMENTS)
                        .join(USER_DOCUMENTS)
                        .on(USER_DOCUMENTS.USER_ID.eq(doc.getUserId()))
                        .and(USER_DOCUMENTS.DOCUMENT_ID.eq(DOCUMENTS.ID))
                        .where(DOCUMENTS.ID.eq(doc.getId()))
                        .fetchOneInto(Integer.class));
        Objects.requireNonNull(id, "document don't exist");
    }

    /**
     * document recover
     *
     * @param doc Document
     * @return recover number
     */
    public Uni<Integer> recover(Document doc) {
        return Uni.createFrom()
            .item(doc)
            .onItem()
            .transform(
                item -> {
                    checkIsExist(doc);
                    List<Integer> ids =
                        DB.get(
                            dsl -> {
                                CommonTableExpression<?> cte =
                                    DSL.name("t")
                                        .as(
                                            DSL.select(DOCUMENTS.ID, DOCUMENTS.PARENT_DOCUMENT_ID)
                                                .from(DOCUMENTS)
                                                .where(DOCUMENTS.ID.eq(doc.getId()))
                                                .and(DOCUMENTS.IS_ARCHIVE.isTrue())
                                                .unionAll(
                                                    DSL.select(DOCUMENTS.ID,
                                                            DOCUMENTS.PARENT_DOCUMENT_ID)
                                                        .from(DOCUMENTS)
                                                        .innerJoin(DSL.table("t"))
                                                        .on(
                                                            DSL.field(
                                                                    DSL.name(
                                                                        "t",
                                                                        DOCUMENTS.PARENT_DOCUMENT_ID
                                                                            .getName()),
                                                                    SQLDataType.INTEGER)
                                                                .eq(DOCUMENTS.ID))
                                                        .and(DOCUMENTS.IS_ARCHIVE.isTrue())));
                                return dsl.withRecursive(cte).selectFrom(cte).fetch(DOCUMENTS.ID);
                            });

                    // TODO: change document isArchive ,need change all archive parents
                    DB.run(
                        dsl -> {
                            // NOTE all children
                            dsl.update(DOCUMENTS)
                                .set(DOCUMENTS.IS_ARCHIVE, false)
                                .where(DOCUMENTS.ID.in(ids))
                                .execute();
                        });
                    log.infof("recover documents,Id:%d,all parentIds:%s", doc.getId(), ids);

                    return ids.size();
                });
    }

    /**
     * documents archive
     *
     * @param doc doc
     * @return archive number
     */
    public Uni<Integer> archive(Document doc) {
        return Uni.createFrom()
            .item(doc)
            .onItem()
            .transform(
                item -> {
                    Document document =
                        DB.get(
                            dsl ->
                                dsl.select(DOCUMENTS)
                                    .from(DOCUMENTS)
                                    .join(USER_DOCUMENTS)
                                    .on(USER_DOCUMENTS.USER_ID.eq(doc.getUserId()))
                                    .and(USER_DOCUMENTS.DOCUMENT_ID.eq(DOCUMENTS.ID))
                                    .where(DOCUMENTS.ID.eq(doc.getId()))
                                    .and(DOCUMENTS.IS_ARCHIVE.isFalse())
                                    .fetchOneInto(Document.class));
                    Objects.requireNonNull(document, "document don't exist");
                    List<Integer> ids =
                        DB.get(
                            dsl -> {
                                CommonTableExpression<?> cte =
                                    DSL.name("t")
                                        .as(
                                            DSL.select(DOCUMENTS.ID)
                                                .from(DOCUMENTS)
                                                .where(DOCUMENTS.ID.eq(doc.getId()))
                                                .and(DOCUMENTS.IS_ARCHIVE.isFalse())
                                                .unionAll(
                                                    DSL.select(DOCUMENTS.ID)
                                                        .from(DOCUMENTS)
                                                        .innerJoin(DSL.table("t"))
                                                        .on(
                                                            DSL.field(
                                                                    DSL.name("t",
                                                                        DOCUMENTS.ID.getName()),
                                                                    SQLDataType.INTEGER)
                                                                .eq(DOCUMENTS.PARENT_DOCUMENT_ID))
                                                        .and(DOCUMENTS.IS_ARCHIVE.isFalse())));
                                return dsl.withRecursive(cte).selectFrom(cte)
                                    .fetchInto(Integer.class);
                            });

                    // TODO: change document isArchive ,need change all children
                    DB.run(
                        dsl -> {
                            // NOTE all children
                            dsl.update(DOCUMENTS)
                                .set(DOCUMENTS.IS_ARCHIVE, true)
                                .where(DOCUMENTS.ID.in(ids))
                                .execute();
                        });
                    log.infof("archive documents,Id:%d,all childrenIds:%s", doc.getId(), ids);

                    return ids.size();
                });
    }

    /**
     * 创建文档
     *
     * @param doc doc
     * @return docId
     */
    public Uni<Integer> create(Document doc) {
        return Uni.createFrom()
            .nullItem()
            .onItem()
            .transform(
                item -> {
                    // NOTE: 引入事务
                    DB.transaction(
                        dsl -> {
                            // NOTE:插入时 忽略id,is_active,is_published字段
                            DocumentsRecord documentsRecord =
                                dsl.insertInto(DOCUMENTS)
                                    .set(DOCUMENTS.TITLE, doc.getTitle())
                                    .set(DOCUMENTS.CONTENT, doc.getContent())
                                    .set(
                                        DOCUMENTS.PARENT_DOCUMENT_ID,
                                        DSL.coalesce(DSL.val(doc.getParentDocumentId())))
                                    .set(DOCUMENTS.COVER_IMAGE,
                                        DSL.coalesce(DSL.val(doc.getCoverImage())))
                                    .set(DOCUMENTS.ICON, DSL.coalesce(DSL.val(doc.getIcon())))
                                    .returning(DOCUMENTS.ID)
                                    .fetchOne();
                            Objects.requireNonNull(documentsRecord, "insert failed");
                            doc.setId(documentsRecord.getId());
                            dsl.insertInto(USER_DOCUMENTS)
                                .set(USER_DOCUMENTS.USER_ID, doc.getUserId())
                                .set(USER_DOCUMENTS.DOCUMENT_ID, documentsRecord.getId())
                                .execute();
                        });
                    log.infof("create document success %s", doc.getId());
                    return doc.getId();
                });
    }

    public Uni<List<Document>> showRootOrChildDocumentsNotArchive(Document doc) {
        doc.setIsArchive(false);
        return showRootOrChildDocuments(doc);
    }

    public Uni<List<Document>> showDocumentsIsArchive(Document doc) {
        return Uni.createFrom()
            .nullItem()
            .onItem()
            .transform(
                item ->
                    DB.get(
                        dsl ->
                            dsl.select(DOCUMENTS)
                                .from(DOCUMENTS)
                                .join(USER_DOCUMENTS)
                                .on(USER_DOCUMENTS.USER_ID.eq(doc.getUserId()))
                                .and(USER_DOCUMENTS.DOCUMENT_ID.eq(DOCUMENTS.ID))
                                .and(DOCUMENTS.IS_ARCHIVE.isTrue())
                                .orderBy(DOCUMENTS.ID.asc())
                                .fetchInto(Document.class)));
    }

    public Uni<List<Document>> showDocumentsNotArchive(Document doc) {
        return Uni.createFrom()
            .nullItem()
            .onItem()
            .transform(
                item ->
                    DB.get(
                        dsl ->
                            dsl.select(DOCUMENTS)
                                .from(DOCUMENTS)
                                .join(USER_DOCUMENTS)
                                .on(USER_DOCUMENTS.USER_ID.eq(doc.getUserId()))
                                .and(USER_DOCUMENTS.DOCUMENT_ID.eq(DOCUMENTS.ID))
                                .and(DOCUMENTS.IS_ARCHIVE.isFalse())
                                .orderBy(DOCUMENTS.ID.asc())
                                .fetchInto(Document.class)));
    }

    Uni<List<Document>> showRootOrChildDocuments(Document doc) {
        return Uni.createFrom()
            .nullItem()
            .onItem()
            .transform(
                item -> {
                    // NOTE:查询时 忽略is_active,is_published字段
                    Condition on;
                    if (doc.getParentDocumentId() == 0) {
                        on = DOCUMENTS.PARENT_DOCUMENT_ID.isNull();
                    } else {
                        on = DOCUMENTS.PARENT_DOCUMENT_ID.eq(doc.getParentDocumentId());
                    }
                    return DB.get(
                        dsl ->
                            dsl.select(DOCUMENTS)
                                .from(DOCUMENTS)
                                .join(USER_DOCUMENTS)
                                .on(USER_DOCUMENTS.USER_ID.eq(doc.getUserId()))
                                .and(USER_DOCUMENTS.DOCUMENT_ID.eq(DOCUMENTS.ID))
                                .where(on)
                                .and(DOCUMENTS.IS_ARCHIVE.eq(doc.getIsArchive()))
                                .orderBy(DOCUMENTS.ID.asc())
                                .fetchInto(Document.class));
                });
    }

    public Uni<Document> findDocumentById(Document doc) {
        return Uni.createFrom()
            .item(doc)
            .onItem()
            .transform(
                item ->
                    DB.get(
                        dsl ->
                            dsl.select(DOCUMENTS)
                                .from(DOCUMENTS)
                                .join(USER_DOCUMENTS)
                                .on(USER_DOCUMENTS.USER_ID.eq(doc.getUserId()))
                                .and(USER_DOCUMENTS.DOCUMENT_ID.eq(DOCUMENTS.ID))
                                .where(DOCUMENTS.ID.eq(item.getId()))
                                .fetchOneInto(Document.class)));
    }

    public Uni<Integer> updateById(Document doc) {
        return Uni.createFrom()
            .item(doc)
            .onItem()
            .transform(
                item ->
                    DB.get(
                        dsl -> {
                            checkIsExist(doc);
                            UpdateSetFirstStep<DocumentsRecord> update = dsl.update(DOCUMENTS);
                            if (!StringUtils.isEmpty(doc.getTitle())) {
                                update.set(DOCUMENTS.TITLE, doc.getTitle());
                            }
                            if (!StringUtils.isEmpty(doc.getContent())) {
                                update.set(DOCUMENTS.CONTENT, doc.getContent());
                            }
                            if (!StringUtils.isEmpty(doc.getCoverImage())) {
                                update.set(DOCUMENTS.COVER_IMAGE, doc.getCoverImage());
                            }
                            if (!StringUtils.isEmpty(doc.getIcon())) {
                                update.set(DOCUMENTS.ICON, doc.getIcon());
                            }
                            if (doc.getIsArchive() != null) {
                                update.set(DOCUMENTS.IS_ARCHIVE, doc.getIsArchive());
                            }
                            if (doc.getIsPublished() != null) {
                                update.set(DOCUMENTS.IS_PUBLISHED, doc.getIsPublished());
                            }

                            try {
                                update
                                    .set(DOCUMENTS.UPDATED_AT, LocalDateTime.now())
                                    .where(DOCUMENTS.ID.eq(doc.getId()))
                                    .execute();
                            } catch (DataAccessException e) {
                                //NOTE: 此并发异常可以忽略
                                return 0;
                            }
                            return 1;
                        }));
    }
}