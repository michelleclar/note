package org.carl.langChain.model;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public record OllamaTool(Type type, Function function) {
    public OllamaTool(
        Type type, Function function) {
        this.type = type;
        this.function = function;
    }

    public static OllamaTool from(
        Function function) {
        return new OllamaTool(Type.FUNCTION, function);
    }

    public Type type() {
        return this.type;
    }

    public Function function() {
        return this.function;
    }

    @JsonDeserialize(
        using = ToolTypeDeserializer.class
    )
    @JsonSerialize(
        using = ToolTypeSerializer.class
    )
    public static enum Type {
        FUNCTION;
    }

    public static record Function(String name, String description, Parameters parameters) {
        public Function(String name, String description, Parameters parameters) {
            this.name = name;
            this.description = description;
            this.parameters = parameters;
        }

        public String name() {
            return this.name;
        }

        public String description() {
            return this.description;
        }

        public Parameters parameters() {
            return this.parameters;
        }

        public static record Parameters(String type, Map<String, Map<String, Object>> properties,
                                        List<String> required) {
            private static final String OBJECT_TYPE = "object";

            public Parameters(String type, Map<String, Map<String, Object>> properties,
                List<String> required) {
                this.type = type;
                this.properties = properties;
                this.required = required;
            }

            public static Parameters objectType(Map<String, Map<String, Object>> properties,
                List<String> required) {
                return new Parameters("object", properties, required);
            }

            public static Parameters empty() {
                return new Parameters("object", Collections.emptyMap(), Collections.emptyList());
            }

            public String type() {
                return this.type;
            }

            public Map<String, Map<String, Object>> properties() {
                return this.properties;
            }

            public List<String> required() {
                return this.required;
            }
        }
    }
}
