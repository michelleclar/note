package org.carl.langChain;

import io.smallrye.mutiny.Multi;
import jakarta.inject.Inject;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import org.carl.aop.annotate.Logged;
import org.carl.langChain.model.OllamaChatRequestPojo;
import org.carl.langChain.model.OllamaRequestPojo;
import org.carl.langChain.model.OllamaResponsePojo;

@Logged
@Path("langChain")
public class LangChainResource {

    @Inject
    LangChainService langChainService;

    // TODO: OllamaRequestPojo need replace CommentRequestPojo
    @POST
    @Path("generate")
    @Produces("application/x-ndjson")
    public Multi<OllamaResponsePojo> generate(OllamaRequestPojo ollamaRequestPojo) {
        return langChainService.generate(ollamaRequestPojo);
    }

    @POST
    @Path("chat")
    @Produces("application/x-ndjson")
    public Multi<OllamaResponsePojo> chat(OllamaChatRequestPojo requestPojo) {
        return langChainService.chat(requestPojo);
    }
}
