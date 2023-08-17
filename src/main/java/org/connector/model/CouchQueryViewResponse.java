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
public class CouchQueryViewResponse {
    @JsonDeserialize(using = JSONAsStringDeserializer.class)
    private String rows;
    private String offset;
    @JsonProperty("total_rows")
    private String totalRows;

    public String getRows() {
        return rows;
    }

    public void setRows(String rows) {
        this.rows = rows;
    }

    public String getOffset() {
        return offset;
    }

    public void setOffset(String offset) {
        this.offset = offset;
    }

    public String getTotalRows() {
        return totalRows;
    }

    public void setTotalRows(String totalRows) {
        this.totalRows = totalRows;
    }

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
