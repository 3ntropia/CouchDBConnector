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
public class BulkGetResponse<T extends Document> {

    private List<BulkGetResponseEntry<T>> results;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonIgnoreProperties(ignoreUnknown = true)
    @Data
    @NoArgsConstructor
    public static class BulkGetResponseEntry<T extends Document> {
        private String id;
        private List<BulkGetEntryDetail<T>> docs;

    }

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonIgnoreProperties(ignoreUnknown = true)
    @Data
    @NoArgsConstructor
    public static class BulkGetEntryDetail<T extends Document> {
        private T ok;
        private BulkGetEntryError error;

    }

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonIgnoreProperties(ignoreUnknown = true)
    public record BulkGetEntryError(
            String id,
            @JsonProperty("rev")
            String revision,
            String error,
            String reason) {

    }
}
