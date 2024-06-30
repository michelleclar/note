package org.carl.auth.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import org.eclipse.microprofile.config.ConfigProvider;

@JsonPropertyOrder({"client_id", "client_secret", "code", "redirect_uri", "access_token"})
public class OAuthGitHubPojo {
  @JsonProperty("client_id")
  String clientId;

  @JsonProperty("client_secret")
  String clientSecret;

  @JsonProperty("code")
  String code;

  @JsonProperty("redirect_uri")
  String redirectUri;

  @JsonProperty("access_token")
  String accessToken;

  @JsonProperty("grant_type")
  String grantType;

  @JsonProperty("refresh_token")
  String refreshToken;

  @JsonProperty("ip")
  String ip;

  @JsonProperty("UUID")
  String UUID;

  public static OAuthGitHubPojo REFRESH() {
    OAuthGitHubPojo pojo = new OAuthGitHubPojo();
    pojo.clientId =
        ConfigProvider.getConfig().getValue("quarkus.oidc-client.client-id", String.class);
    pojo.clientSecret =
        ConfigProvider.getConfig().getValue("quarkus.oidc-client.credentials.secret", String.class);
    pojo.grantType = "refresh_token";
    return pojo;
  }

  public String getClientId() {
    return clientId;
  }

  public String getClientSecret() {
    return clientSecret;
  }

  public String getCode() {
    return code;
  }

  public void setCode(String code) {
    this.code = code;
  }

  public String getRedirectUri() {
    return redirectUri;
  }

  public void setRedirectUri(String redirectUri) {
    this.redirectUri = redirectUri;
  }

  public String getAccessToken() {
    return accessToken;
  }

  public void setAccessToken(String accessToken) {
    this.accessToken = accessToken;
  }

  public void setClientId(String clientId) {
    this.clientId = clientId;
  }

  public void setClientSecret(String clientSecret) {
    this.clientSecret = clientSecret;
  }

  public String getRefreshToken() {
    return refreshToken;
  }

  public void setRefreshToken(String refreshToken) {
    this.refreshToken = refreshToken;
  }

  public String getGrantType() {
    return grantType;
  }

  public String getIp() {
    return ip;
  }

  public void setIp(String ip) {
    this.ip = ip;
  }

  public void setGrantType(String grantType) {
    this.grantType = grantType;
  }

  public String getUUID() {
    return UUID;
  }

  public void setUUID(String uUID) {
    UUID = uUID;
  }
}
