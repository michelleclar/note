package org.carl.langChain.model;


import java.util.List;

public record OllamaMessage(OllamaRole role, String content, List<OllamaToolCall> toolCalls,
                            List<String> images) {
    public OllamaMessage(OllamaRole role, String content, List<OllamaToolCall> toolCalls,
        List<String> images) {
        this.role = role;
        this.content = content;
        this.toolCalls = toolCalls;
        this.images = images;
    }

    public static Builder builder() {
        return new Builder();
    }

    public OllamaRole role() {
        return this.role;
    }

    public String content() {
        return this.content;
    }

    public List<OllamaToolCall> toolCalls() {
        return this.toolCalls;
    }

    public List<String> images() {
        return this.images;
    }

    public static class Builder {
        private OllamaRole role;
        private String content;
        private List<OllamaToolCall> toolCalls;
        private List<String> images;

        public Builder() {
        }

        public Builder role(OllamaRole role) {
            this.role = role;
            return this;
        }

        public Builder content(String content) {
            this.content = content;
            return this;
        }

        public Builder toolCalls(List<OllamaToolCall> toolCalls) {
            this.toolCalls = toolCalls;
            return this;
        }

        public Builder images(List<String> images) {
            this.images = images;
            return this;
        }

        public OllamaMessage build() {
            return new OllamaMessage(this.role, this.content, this.toolCalls, this.images);
        }
    }
}
