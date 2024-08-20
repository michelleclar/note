package org.carl.langChain.model;

import java.util.Map;

public record OllamaToolCall(FunctionCall function) {
    public OllamaToolCall(FunctionCall function) {
        this.function = function;
    }

    public static OllamaToolCall fromFunctionCall(String name, Map<String, Object> arguments) {
        return new OllamaToolCall(new FunctionCall(name, arguments));
    }

    public FunctionCall function() {
        return this.function;
    }

    public static record FunctionCall(String name, Map<String, Object> arguments) {
        public FunctionCall(String name, Map<String, Object> arguments) {
            this.name = name;
            this.arguments = arguments;
        }

        public String name() {
            return this.name;
        }

        public Map<String, Object> arguments() {
            return this.arguments;
        }
    }
}