package org.connector.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.TreeNode;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import org.connector.util.JSON;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public record CouchQueryViewResponse(
        @JsonDeserialize(using = JSONAsStringDeserializer.class) String rows,
        String offset,
        @JsonProperty("total_rows") String totalRows) {

    public List<Row> getDeserializedRow(){
        return JSON.fromJson(this.rows, JSON.getCollectionType(ArrayList.class, Row.class));
    }

    public static class JSONAsStringDeserializer extends StdDeserializer<String> {
        public JSONAsStringDeserializer(Class<?> vc) {
            super(vc);
        }
        public JSONAsStringDeserializer(){
            this(null);
        }
        public String deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException, JsonProcessingException {
            TreeNode tree = jsonParser.getCodec().readTree(jsonParser);
            return tree.toString();
        }
    }

}
