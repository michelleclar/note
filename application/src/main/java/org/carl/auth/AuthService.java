package org.carl.auth;

import io.quarkus.cache.Cache;
import io.quarkus.cache.CacheName;
import io.quarkus.runtime.StartupEvent;
import io.smallrye.mutiny.Uni;
import io.vertx.core.http.HttpServerRequest;
import io.vertx.core.json.JsonObject;
import io.vertx.mutiny.core.eventbus.EventBus;
import jakarta.enterprise.event.Observes;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import jakarta.ws.rs.core.Response;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;
import org.carl.auth.client.OAuthClient;
import org.carl.auth.client.RestClientWithTokenHeaderParam;
import org.carl.auth.model.OAuthGitHubPojo;
import org.carl.auth.model.Providers;
import org.carl.cache.CacheFields;
import org.carl.commons.Fields;
import org.carl.generated.tables.pojos.OauthProviders;
import org.carl.generated.tables.records.OauthProvidersRecord;
import org.carl.engine.DB;
import org.carl.listen.ListenFields;
import org.carl.user.UserService;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.jboss.logging.Logger;

import static org.carl.generated.Tables.OAUTH_PROVIDERS;


@Singleton
public class AuthService {
    private final Logger log = Logger.getLogger(UserService.class);
    Map<Providers, OauthProviders> oauthProviders;

    @CacheName(CacheFields.OAUTH_GITHUB)
    public Cache oauthCache;

    @Inject
    EventBus bus;

    @Inject
    @RestClient
    OAuthClient oAuthClient;
    @Inject
    @RestClient
    RestClientWithTokenHeaderParam restClient;
    @Inject
    DB DB;


    public String github(HttpServerRequest request, String code) {
        OAuthGitHubPojo o = new OAuthGitHubPojo();
        o.setCode(code);
        o.setIp(request.remoteAddress().host());
        o.setUUID(UUID.randomUUID().toString());
        OauthProviders providers = oauthProviders.get(Providers.GITHUB);
        o.setClientId(providers.getClientId());
        o.setClientSecret(providers.getClientSecret());

        // TODO: To resolve the 401 issue, proxy problems are not considered for now.
        try {
            // NOTE: get access_token
            Response accessToken = oAuthClient.callGitHubToken(o);
            if (accessToken.getStatus() == 401) {
                log.warnf(
                    "♠️♠️♠️ A malicious attack check is required for the attack interface "
                        + "GithubOAuth.,ip:%s",
                    o.getIp());
                return null;
            }
            JsonObject json = new JsonObject(accessToken.readEntity(String.class));
            String token = json.getString(Fields.ACCESS_TOKEN);
            if (token == null) {
                log.warnf(
                    "♠️♠️♠️ A malicious attack check is required for the attack interface "
                        + "GithubOAuth.,ip:%s",
                    o.getIp());
                return null;
            }

            // NOTE: get userInfo
            // NOTE: Extreme cases are not considered. Currently, the only error scenario is the
            // proxy issue.
            Response userInfo = restClient.getUserInfo(Fields.BEARER + token);
            JsonObject userInfoJson = new JsonObject(userInfo.readEntity(String.class));
            userInfoJson.put(Fields.ACCESS_TOKEN, token).put(Fields.PROVIDER_ID, 1)
                .put(Fields.CODE, o.getCode()).put(Fields.UUID, o.getUUID());
            // TODO insert user info to db
            bus.request(ListenFields.USER_REGISTER, userInfoJson).subscribe()
                .with(message -> {
                    log.infof(
                        "⛵ User registration successful,User registration by Github. userId:%s",
                        message.body());
                });

            return o.getUUID();
        } catch (Exception e) {
            log.errorf("⛵⛵⛵ User registration error: %s,code %s", e.getMessage(), code, e);
            throw new RuntimeException(e);
        }
    }

    void onStarted(@Observes StartupEvent startupEvent) {

        initOAuthProviders();
        List<OauthProviders> l =
            DB.get(dsl -> dsl.selectFrom(OAUTH_PROVIDERS).fetch().into(OauthProviders.class));
        oauthProviders =
            l.stream().collect(Collectors.toMap(o -> Providers.of(o.getName()), p -> p));
    }

    // NOTE:Automatic initialization of third-party login information will not be performed at this
    // time. Manual insertion is required. Logs will be checked and printed based on the Providers
    // enum.
    private void initOAuthProviders() {
        for (Providers p : Providers.values()) {
            OauthProvidersRecord record = DB.get(dsl -> dsl.selectFrom(OAUTH_PROVIDERS)
                .where(OAUTH_PROVIDERS.NAME.eq(p.providersName)).fetchOne());
            if (record == null) {
                log.warn("Please insert " + p.providersName + " into oauth_providers table");
            }
        }
    }

    public Uni<Map<Providers, OauthProviders>> getAuthContext() {
        return Uni.createFrom().item(oauthProviders).onItem().transform(item -> {
            if (item != null) {
                return item;
            }
            List<OauthProviders> l =
                DB.get(dsl -> dsl.selectFrom(OAUTH_PROVIDERS).fetch().into(OauthProviders.class));
            oauthProviders =
                l.stream().collect(Collectors.toMap(o -> Providers.of(o.getName()), p -> p));
            return oauthProviders;
        });
    }
}
