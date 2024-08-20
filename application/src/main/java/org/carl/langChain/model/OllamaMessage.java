package org.carl.langChain.model;

import io.quarkiverse.langchain4j.ollama.Role;
import io.quarkiverse.langchain4j.ollama.ToolCall;
import java.util.List;

public record OllamaMessage(Role role, String content, List<ToolCall> toolCalls, List<String> images) {
    public OllamaMessage(Role role, String content, List<ToolCall> toolCalls, List<String> images) {
        this.role = role;
        this.content = content;
        this.toolCalls = toolCalls;
        this.images = images;
    }

    public static io.quarkiverse.langchain4j.ollama.Message.Builder builder() {
        return new io.quarkiverse.langchain4j.ollama.Message.Builder();
    }

    public Role role() {
        return this.role;
    }

    public String content() {
        return this.content;
    }

    public List<ToolCall> toolCalls() {
        return this.toolCalls;
    }

    public List<String> images() {
        return this.images;
    }

    public static class Builder {
        private Role role;
        private String content;
        private List<ToolCall> toolCalls;
        private List<String> images;

        public Builder() {
        }

        public Builder role(Role role) {
            this.role = role;
            return this;
        }

        public Builder content(String content) {
            this.content = content;
            return this;
        }

        public Builder toolCalls(List<ToolCall> toolCalls) {
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
