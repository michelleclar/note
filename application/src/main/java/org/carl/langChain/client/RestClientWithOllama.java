package org.carl.langChain.client;

import io.smallrye.mutiny.Multi;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.MediaType;
import org.carl.langChain.model.OllamaChatRequestPojo;
import org.carl.langChain.model.OllamaRequestPojo;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;
import org.jboss.resteasy.reactive.RestStreamElementType;

@RegisterRestClient(configKey = "llm",baseUri = "http://localhost:11434/api")
@Path("/")
public interface RestClientWithOllama {

    @POST
    @Path("chat")
    @RestStreamElementType(MediaType.TEXT_PLAIN)
    Multi<String> chat(OllamaChatRequestPojo ollamaChatRequestPojo);

    @POST
    @Path("generate")
    @RestStreamElementType(MediaType.APPLICATION_JSON)
    Multi<String> generate(OllamaRequestPojo ollamaRequestPojo);
}
