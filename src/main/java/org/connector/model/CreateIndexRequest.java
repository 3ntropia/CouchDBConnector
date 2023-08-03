package org.connector.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateIndexRequest {
    @Builder.Default
    private IndexDefinition index = new IndexDefinition();
    private String ddoc;
    private String name;
    @Builder.Default
    private String type = "json";
    private Boolean partitioned;
}
