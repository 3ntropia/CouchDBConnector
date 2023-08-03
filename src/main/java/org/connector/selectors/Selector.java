package org.connector.selectors;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.connector.util.JSON;

import java.io.IOException;

@JsonSerialize(using = Selector.SelectorSerializer.class)
public abstract class Selector {
    private final String key;
    private final Object value;

    protected Selector(String key, Object value) {
        this.key = key;
        this.value = value;
    }

    protected Selector(Operator operator, Object value) {
        this.key = operator.getOp();
        this.value = value;
    }

    protected <S extends Selector> Selector(String key, S selector) {
        this.key = key;
        this.value = selector;
    }

    protected <S extends Selector> Selector(Operator operator, S selector) {
        this.key = operator.getOp();
        this.value = selector;
    }

    static class SelectorSerializer extends JsonSerializer<Selector> {
        @Override
        public void serialize(Selector selector, JsonGenerator gen, SerializerProvider serializers) throws IOException {
            gen.writeStartObject();
            gen.writeObjectField(selector.key, selector.value);
            gen.writeEndObject();
        }
    }

    public String asJson() {
        return JSON.toJson(this);
    }
}
