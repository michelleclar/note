package org.carl.auth.client;

import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.carl.auth.model.OAuthGitHubPojo;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

@RegisterRestClient(configKey = "oauth")
@Path("/oauth")
public interface OAuthClient {
  @POST
  @Path("/access_token")
  @Produces(MediaType.APPLICATION_JSON)
  Response callGitHubToken(OAuthGitHubPojo oAuthGitHubPojo);
}
