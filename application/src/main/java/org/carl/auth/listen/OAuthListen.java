package org.carl.auth.listen;

import io.quarkus.vertx.ConsumeEvent;
import io.vertx.mutiny.core.eventbus.EventBus;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.carl.auth.AuthService;
import org.carl.auth.client.OAuthClient;
import org.carl.auth.client.RestClientWithTokenHeaderParam;
import org.carl.auth.model.OAuthGitHubPojo;
import org.carl.listen.ListenFields;
import org.eclipse.microprofile.rest.client.inject.RestClient;

@ApplicationScoped
public class OAuthListen {
  @Inject
  @RestClient
  OAuthClient oAuthClient;
  @Inject
  @RestClient
  RestClientWithTokenHeaderParam restClient;

  @Inject
  EventBus bus;
  @Inject
  AuthService authService;

  @ConsumeEvent(ListenFields.OAUTH_GITHUB)
  public void GetGitHubInfo(OAuthGitHubPojo oAuthGitHubPojo) {}
}
