package org.carl.auth;

import io.smallrye.common.annotation.Blocking;
import io.smallrye.mutiny.Uni;
import io.vertx.core.http.HttpServerRequest;
import jakarta.annotation.security.PermitAll;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.NewCookie;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriBuilder;
import org.carl.commons.Fields;
import org.eclipse.microprofile.config.inject.ConfigProperty;

import java.util.Date;

@Path("/oauth")
public class AuthResource {

    @Inject
    AuthService authService;
    @ConfigProperty(name = "web.host")
    String host;
    @ConfigProperty(name = "web.port")
    Integer port;

    @GET
    @Path("github")
    @PermitAll
    @Blocking
    public Uni<Response> github(@Context HttpServerRequest request, @QueryParam("code") String code) {
        return Uni.createFrom().nullItem().onItem().transform(i -> {
            String uuid = authService.github(request, code);
            if (uuid == null) {
                return Response.seeOther(UriBuilder.fromUri("/").host(host).port(port).build())
                        .build();
            }
            NewCookie _cookie = new NewCookie.Builder(Fields.CODE).value(uuid).path("/")
                    .domain("localhost").maxAge(60 * 5).expiry(new Date(System.currentTimeMillis() + 30000))
                    .sameSite(NewCookie.SameSite.NONE).httpOnly(true).secure(true).build();
            return Response
                    .seeOther(UriBuilder.fromUri("/documents").host(host).port(port).build())
                    .cookie(_cookie).build();
        });
    }
}
