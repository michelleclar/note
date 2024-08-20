package org.carl.langChain.model;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import java.io.IOException;
import java.util.Locale;

public class ToolTypeDeserializer extends StdDeserializer<OllamaTool.Type> {
    public ToolTypeDeserializer() {
        super(OllamaTool.Type.class);
    }

    public OllamaTool.Type deserialize(JsonParser jp, DeserializationContext deserializationContext) throws
        IOException {
        return OllamaTool.Type.valueOf(jp.getValueAsString().toUpperCase(Locale.ROOT));
    }
}