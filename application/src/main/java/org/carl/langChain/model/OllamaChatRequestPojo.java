package org.carl.langChain.model;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

public class OllamaChatRequestPojo {
    String model;
    List<OllamaMessage> messages;
    List<OllamaTool> tools;
    String format;
    List<OllamaOptions> options;
    String stream;
    @JsonAlias({"keep_alive", "keepAlive"})
    @JsonProperty("keep_alive")
    String keepAlive;
}