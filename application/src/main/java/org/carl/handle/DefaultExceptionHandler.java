package org.carl.handle;

import io.quarkus.security.UnauthorizedException;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.ServerErrorException;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriBuilder;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;
import org.carl.auth.exception.TokenExpiration;
import org.carl.commons.Fields;
import org.carl.commons.R;
import org.carl.commons.AppStatus;
import org.carl.user.exception.UserLoginException;
import org.jboss.logging.Logger;

@Provider
public class DefaultExceptionHandler implements ExceptionMapper<Exception> {
    private final Logger log = Logger.getLogger(DefaultExceptionHandler.class);

    @Override
    @Produces(MediaType.APPLICATION_JSON)
    public Response toResponse(Exception e) {

        if (e instanceof UserLoginException) {
            return Response.status(AppStatus.LOGIN_ERROR)
                    .entity(new R(AppStatus.LOGIN_ERROR.getReasonPhrase()))
                    .type(MediaType.APPLICATION_JSON)
                    .build();
        }
        if (e instanceof ServerErrorException) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(new R(Response.Status.INTERNAL_SERVER_ERROR.getReasonPhrase()))
                    .type(MediaType.APPLICATION_JSON).build();
        }
        if (e instanceof TokenExpiration) {
            return Response.seeOther(UriBuilder.fromUri("/").host(Fields.WEB_URI.getHost()).port(Fields.WEB_URI.getPort()).build())
                    .status(Response.Status.UNAUTHORIZED).build();
        }
        if (e instanceof UnauthorizedException) {
            return Response.seeOther(UriBuilder.fromUri("/").host(Fields.WEB_URI.getHost()).port(Fields.WEB_URI.getPort()).build())
                    .status(Response.Status.UNAUTHORIZED).build();
        }

        log.error("Unhandled exception.", e.getMessage(), e);
        return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage())
                .type(MediaType.TEXT_PLAIN).build();
    }
}

