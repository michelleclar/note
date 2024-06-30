package org.carl.documents;

import io.netty.util.internal.ObjectUtil;
import io.smallrye.mutiny.Uni;
import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.List;
import org.carl.aop.annotate.Logged;
import org.carl.auth.utils.JwtUtils;
import org.carl.documents.model.Document;
import org.eclipse.microprofile.jwt.JsonWebToken;
import org.jboss.resteasy.reactive.RestResponse;

@Logged
@Path("/documents")
@RolesAllowed({"user", "admin"})
public class DocumentsResource {
    @Inject
    DocumentsService documentsService;
    @Inject
    JsonWebToken jwt;

    @POST
    @Path("create")
    public Uni<Response> create(Document doc) {

        Integer userId = Integer.parseInt(jwt.getClaim(JwtUtils.USER_ID).toString());
        doc.setUserId(userId);
        return documentsService
            .create(doc)
            .onItem()
            .transform(item -> Response.ok(item).type(MediaType.TEXT_PLAIN).build());
    }

    @GET
    @Path("showDocumentsIsArchive")
    public Uni<RestResponse<List<Document>>> showDocumentsIsArchive() {
        Integer userId = Integer.parseInt(jwt.getClaim(JwtUtils.USER_ID).toString());
        Document document = new Document();
        document.setUserId(userId);
        return documentsService
            .showDocumentsIsArchive(document)
            .onItem()
            .transform(
                item -> RestResponse.ResponseBuilder.ok(item).type(MediaType.APPLICATION_JSON)
                    .build());
    }

    @GET
    @Path("showDocumentsNotArchive")
    public Uni<RestResponse<List<Document>>> showDocumentsNotArchive() {
        Integer userId = Integer.parseInt(jwt.getClaim(JwtUtils.USER_ID).toString());
        Document document = new Document();
        document.setUserId(userId);
        return documentsService
            .showDocumentsNotArchive(document)
            .onItem()
            .transform(
                item -> RestResponse.ResponseBuilder.ok(item).type(MediaType.APPLICATION_JSON)
                    .build());
    }

    @GET
    @Path("showRootOrChildDocumentsNotArchive/{parentDocumentId}")
    public Uni<RestResponse<List<Document>>> showRootOrChildDocumentsNotArchive(
        @PathParam("parentDocumentId") Integer parentDocumentId) {
        Integer userId = Integer.parseInt(jwt.getClaim(JwtUtils.USER_ID).toString());
        Document document = new Document();
        document.setUserId(userId);
        document.setParentDocumentId(parentDocumentId);
        return documentsService
            .showRootOrChildDocumentsNotArchive(document)
            .onItem()
            .transform(
                item -> RestResponse.ResponseBuilder.ok(item).type(MediaType.APPLICATION_JSON)
                    .build());
    }

    @GET
    @Path("archive/{documentId}")
    public Uni<Response> archive(@PathParam("documentId") Integer documentId) {
        Integer userId = Integer.parseInt(jwt.getClaim(JwtUtils.USER_ID).toString());
        Document document = new Document();
        document.setId(documentId);
        document.setUserId(userId);
        return documentsService.archive(document).onItem().transform(item -> Response.ok().build());
    }

    @GET
    @Path("recover/{documentId}")
    public Uni<Response> recover(@PathParam("documentId") Integer documentId) {
        Integer userId = Integer.parseInt(jwt.getClaim(JwtUtils.USER_ID).toString());
        Document document = new Document();
        document.setId(documentId);
        document.setUserId(userId);
        return documentsService.recover(document).onItem().transform(item -> Response.ok().build());
    }

    @GET
    @Path("remove/{documentId}")
    public Uni<Response> remove(@PathParam("documentId") Integer documentId) {
        Integer userId = Integer.parseInt(jwt.getClaim(JwtUtils.USER_ID).toString());
        Document document = new Document();
        document.setId(documentId);
        document.setUserId(userId);
        return documentsService.remove(document).onItem().transform(item -> Response.ok().build());
    }

    @GET
    @Path("removeIcon/{documentId}")
    public Uni<Response> removeIcon(@PathParam("documentId") Integer documentId) {
        Integer userId = Integer.parseInt(jwt.getClaim(JwtUtils.USER_ID).toString());
        Document document = new Document();
        document.setId(documentId);
        document.setUserId(userId);
        return documentsService.removeIcon(document).onItem()
            .transform(item -> Response.ok().build());
    }

    @GET
    @Path("removeCoverImage/{documentId}")
    public Uni<Response> removeCoverImage(@PathParam("documentId") Integer documentId) {
        Integer userId = Integer.parseInt(jwt.getClaim(JwtUtils.USER_ID).toString());
        Document document = new Document();
        document.setId(documentId);
        document.setUserId(userId);
        return documentsService
            .removeCoverImage(document)
            .onItem()
            .transform(item -> Response.ok().build());
    }

    @GET
    @Path("findById/{documentId}")
    public Uni<RestResponse<Document>> findById(@PathParam("documentId") Integer documentId) {
        Integer userId = Integer.parseInt(jwt.getClaim(JwtUtils.USER_ID).toString());
        Document document = new Document();
        document.setId(documentId);
        document.setUserId(userId);
        return documentsService
            .findDocumentById(document)
            .onItem()
            .transform(
                item -> {
                    ObjectUtil.checkNotNull(item, "document not exist");
                    return RestResponse.ResponseBuilder.ok(item).type(MediaType.APPLICATION_JSON)
                        .build();
                });
    }

    @POST
    @Path("updateById")
    @Produces(MediaType.APPLICATION_JSON)
    public Uni<Response> updateById(Document doc) {
        Integer userId = Integer.parseInt(jwt.getClaim(JwtUtils.USER_ID).toString());
        doc.setUserId(userId);
        return documentsService.updateById(doc).onItem().transform(item -> Response.ok().build());
    }
}