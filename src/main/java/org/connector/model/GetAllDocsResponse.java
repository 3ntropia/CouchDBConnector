package org.connector.model;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

/**
 * Get All Docs
 * https://docs.couchdb.org/en/latest/api/database/bulk-api.html#db-all-docs
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public record GetAllDocsResponse(
        @JsonProperty("total_rows") Long totalRows,
        Long offset,
        List<Row> rows) {

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonIgnoreProperties(ignoreUnknown = true)
    public record Row(
            Value value,
            String id,
            String key) {
    }

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonIgnoreProperties(ignoreUnknown = true)
    public record Value(String rev) {
    }
}
