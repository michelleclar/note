package org.carl.langChain;

import io.smallrye.mutiny.Multi;
import io.vertx.core.json.JsonObject;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import org.apache.commons.lang3.StringUtils;
import org.carl.langChain.client.RestClientWithOllama;
import org.carl.langChain.model.OllamaChatRequestPojo;
import org.carl.langChain.model.OllamaRequestPojo;
import org.carl.langChain.model.OllamaResponsePojo;
import org.carl.utils.LogUtil;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.jboss.logging.Logger;

@Singleton
public class LangChainService {
    private final Logger log = Logger.getLogger(LangChainService.class);

    //TODO: need replace comment RestClient
    @Inject
    @RestClient
    RestClientWithOllama restClientWithOllama;

    String incompleteResponse = "";

    //TODO: need adaptation all model request
    //TODO: add handle function ollama response
    public Multi<OllamaResponsePojo> generate(OllamaRequestPojo ollamaRequestPojo) {
        return restClientWithOllama.generate(ollamaRequestPojo).onItem().transform(item -> {
            try {
                if (StringUtils.isBlank(incompleteResponse) && item.endsWith("}")) {
                    JsonObject object = new JsonObject(item);
                    return object.mapTo(OllamaResponsePojo.class);
                }
                if (!StringUtils.isBlank(incompleteResponse) && item.endsWith("}")) {
                    incompleteResponse += item;
                    log.infof("Incomplete response: %s", incompleteResponse);
                    JsonObject object = new JsonObject(incompleteResponse);
                    OllamaResponsePojo ollamaResponsePojo =
                        object.mapTo(OllamaResponsePojo.class);
                    incompleteResponse = StringUtils.EMPTY;
                    return ollamaResponsePojo;
                }
                incompleteResponse += item;
                return OllamaResponsePojo.EMPTY;
            } catch (Exception e) {
                log.errorf("source string is : %s", LogUtil.beanToString(item));
                return OllamaResponsePojo.EMPTY;
            }
        }).filter(item -> !StringUtils.isBlank(item.getResponse()));
    }

    public Multi<OllamaResponsePojo> chat(OllamaChatRequestPojo requestPojo) {
        return restClientWithOllama.chat(requestPojo).onItem().transform(item -> {
            try {
                if (StringUtils.isBlank(incompleteResponse) && item.endsWith("}")) {
                    JsonObject object = new JsonObject(item);
                    return object.mapTo(OllamaResponsePojo.class);
                }
                if (!StringUtils.isBlank(incompleteResponse) && item.endsWith("}")) {
                    incompleteResponse += item;
                    log.infof("Incomplete response: %s", incompleteResponse);
                    JsonObject object = new JsonObject(incompleteResponse);
                    OllamaResponsePojo ollamaResponsePojo =
                        object.mapTo(OllamaResponsePojo.class);
                    incompleteResponse = StringUtils.EMPTY;
                    return ollamaResponsePojo;
                }
                incompleteResponse += item;
                return OllamaResponsePojo.EMPTY;
            } catch (Exception e) {
                log.errorf("source string is : %s", LogUtil.beanToString(item));
                return OllamaResponsePojo.EMPTY;
            }
        }).filter(item -> !StringUtils.isBlank(item.getResponse()));
    }
}
