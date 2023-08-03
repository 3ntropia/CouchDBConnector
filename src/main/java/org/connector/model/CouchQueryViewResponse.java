package org.connector.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import org.connector.model.serialization.JSONAsStringDeserializer;
import com.marvel.util.JSON;

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
}
