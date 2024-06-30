package org.carl.auth.client;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.HeaderParam;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

@RegisterRestClient(configKey = "oauth")
@Path("/")
public interface RestClientWithTokenHeaderParam {

  @GET
  @Path("user")
  @Produces(MediaType.APPLICATION_JSON)
  Response getUserInfo(@HeaderParam("Authorization") String authorization);
}

