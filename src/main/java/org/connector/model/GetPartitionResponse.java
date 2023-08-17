package org.connector.model;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public record GetPartitionResponse(
        @JsonProperty("db_name") String dbName,
        @JsonProperty("doc_count") Integer docCount,
        @JsonProperty("doc_del_count") Integer docDelCount,
        List<Size> sizes) {

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonIgnoreProperties(ignoreUnknown = true)
    public record Size(
            Integer active,
            Integer external) {

    }
}
