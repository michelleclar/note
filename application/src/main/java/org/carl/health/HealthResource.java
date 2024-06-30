package org.carl.health;

import jakarta.annotation.security.PermitAll;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.carl.aop.annotate.Logged;

@Path("/health")
@Logged
public class HealthResource {
    @GET
    @Path("/check")
    @PermitAll
    @Produces(MediaType.APPLICATION_JSON)
    public Response check() {
        return Response.ok().build();
    }
}
