package org.connector.model;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@Data
@NoArgsConstructor
public class GetPartitionResponse {
    @JsonProperty("db_name")
    private String dbName;
    @JsonProperty("doc_count")
    Integer docCount;
    @JsonProperty("doc_del_count")
    private Integer docDelCount;
    private List<Size> sizes;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonIgnoreProperties(ignoreUnknown = true)
    @Data
    @NoArgsConstructor
    public static class Size {
        private Integer active;
        private Integer external;
    }
}
