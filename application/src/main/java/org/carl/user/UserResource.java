package org.carl.user;

import io.smallrye.mutiny.Uni;
import io.smallrye.mutiny.unchecked.Unchecked;
import io.vertx.core.http.HttpServerRequest;
import io.vertx.mutiny.core.eventbus.EventBus;
import jakarta.annotation.security.PermitAll;
import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;
import org.carl.aop.annotate.Logged;
import org.carl.auth.utils.JwtUtils;
import org.carl.commons.Fields;
import org.carl.user.exception.RetryException;
import org.carl.listen.ListenFields;
import org.carl.user.model.User;
import org.eclipse.microprofile.jwt.JsonWebToken;
import org.jboss.resteasy.reactive.RestResponse;

@Logged
@Path("/user")
public class UserResource {

    @Inject
    EventBus bus;
    @Inject
    UserService userService;

    @Inject
    JsonWebToken jwt;

    @GET
    @Path("toAuth")
    @PermitAll
    public Uni<Response> toAuth(@Context HttpServerRequest request) {
        return userService.toAuth(request.getCookie(Fields.CODE).getValue()).onItem()
            .transform(item -> item == null ? Response.status(Status.UNAUTHORIZED).build()
                : Response.ok().entity(item).build());
    }

    @POST
    @Path("/register")
    @PermitAll
    @Produces(MediaType.APPLICATION_JSON)
    public Uni<RestResponse<JwtUtils.JwtPojo>> register(User user) {
        return userService.register(user).onItem().transform(Unchecked.function(item -> {
            if (item.getId() == null) {
                throw new RetryException("Please retry.");
            }

            bus.send(ListenFields.CLEAN_CACHE_CODE, item.getEmail());
            return RestResponse.ResponseBuilder.ok(JwtUtils.generateToken(item))
                .type(MediaType.APPLICATION_JSON)
                .build();
        }));
    }


    @GET
    @Path("getUserInfo")
    @RolesAllowed({"user", "admin"})
    @Produces(MediaType.APPLICATION_JSON)
    public Uni<RestResponse<User>> getUserInfo() {

        return Uni.createFrom().nullItem().onItem().transform(item -> {
            Integer id = Integer.parseInt(jwt.getClaim(JwtUtils.USER_ID).toString());
            String username = jwt.getClaim(JwtUtils.USERNAME);
            String email = jwt.getClaim(JwtUtils.EMAIl);
            String imageUrl = jwt.getClaim(JwtUtils.IMAGE_URL);
            User user = new User();
            user.setId(id);
            user.setUsername(username);
            user.setEmail(email);
            user.setImageUrl(imageUrl);
            return RestResponse.ResponseBuilder.ok(user).type(MediaType.APPLICATION_JSON).build();

        });
    }

    @GET
    @Path("refreshToken")
    @PermitAll
    @Produces(MediaType.APPLICATION_JSON)
    public Uni<RestResponse<JwtUtils.JwtPojo>> refreshToken(@Context HttpServerRequest request) {
        String accessToken = request.getHeader(Fields.AUTHORIZATION);
        String refreshToken = request.getHeader(Fields.REFRESH_TOKEN);
        return userService.refreshToken(refreshToken,accessToken).onItem()
            .transform(Unchecked.function(item -> RestResponse.ResponseBuilder.ok(item)
                .type(MediaType.APPLICATION_JSON)
                .build()));

    }


}