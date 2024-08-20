package org.carl.langChain.model;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import java.io.IOException;
import java.util.Locale;

public class ToolTypeSerializer extends StdSerializer<OllamaTool.Type> {
    public ToolTypeSerializer() {
        super(OllamaTool.Type.class);
    }

    public void serialize(OllamaTool.Type toolType, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws
        IOException {
        jsonGenerator.writeString(toolType.toString().toLowerCase(Locale.ROOT));
    }
}
