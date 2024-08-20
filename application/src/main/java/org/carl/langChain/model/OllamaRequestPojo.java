package org.carl.langChain.model;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonProperty;

// NOTE: https://github.com/ollama/ollama/blob/main/docs/api.md#request-json-mode
public class OllamaRequestPojo {

    //(required) the model name
    String model;
    //the prompt to generate a response for
    String prompt;
    //the text after the model response
    String suffix;
    //(optional) a list of base64-encoded images (for multimodal models such as llava)
    String images;

    //the format to return a response in. Currently the only accepted value is json
    String format;
    //additional model parameters listed in the documentation for the Modelfile such as temperature
    String options;
    //system message to (overrides what is defined in the Modelfile)
    String system;
    //the prompt template to use (overrides what is defined in the Modelfile)
    String template;
    //the context parameter returned from a previous request to /generate, this can be used to
    // keep a short conversational memory
    String context;
    //if false the response will be returned as a single response object, rather than a stream of
    // objects
    String stream;
    //if true no formatting will be applied to the prompt. You may choose to use the raw
    // parameter if you are specifying a full templated prompt in your request to the API
    String raw;
    //controls how long the model will stay loaded into memory following the request (default: 5m)
    @JsonAlias({"keep_alive", "keepAlive"})
    @JsonProperty("keep_alive")
    String keepAlive;

    public OllamaRequestPojo setModel(String model) {
        this.model = model;
        return this;
    }

    public OllamaRequestPojo setPrompt(String prompt) {
        this.prompt = prompt;
        return this;
    }

    public OllamaRequestPojo setSuffix(String suffix) {
        this.suffix = suffix;
        return this;
    }

    public OllamaRequestPojo setImages(String images) {
        this.images = images;
        return this;
    }

    public OllamaRequestPojo setFormat(String format) {
        this.format = format;
        return this;
    }

    public OllamaRequestPojo setOptions(String options) {
        this.options = options;
        return this;
    }

    public OllamaRequestPojo setSystem(String system) {
        this.system = system;
        return this;
    }

    public OllamaRequestPojo setTemplate(String template) {
        this.template = template;
        return this;
    }

    public OllamaRequestPojo setContext(String context) {
        this.context = context;
        return this;
    }

    public OllamaRequestPojo setStream(String stream) {
        this.stream = stream;
        return this;
    }

    public OllamaRequestPojo setRaw(String raw) {
        this.raw = raw;
        return this;
    }

    public OllamaRequestPojo setKeep_alive(String keep_alive) {
        this.keepAlive = keep_alive;
        return this;
    }

    public String getModel() {
        return model;
    }

    public String getPrompt() {
        return prompt;
    }

    public String getSuffix() {
        return suffix;
    }

    public String getImages() {
        return images;
    }

    public String getFormat() {
        return format;
    }

    public String getOptions() {
        return options;
    }

    public String getSystem() {
        return system;
    }

    public String getTemplate() {
        return template;
    }

    public String getContext() {
        return context;
    }

    public String getStream() {
        return stream;
    }

    public String getRaw() {
        return raw;
    }

    public String getKeep_alive() {
        return keepAlive;
    }
}
