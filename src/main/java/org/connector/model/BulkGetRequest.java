package org.connector.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@Data
@NoArgsConstructor
public class BulkGetRequest {
    private List<BulkGetEntry> docs = new ArrayList<>();

    public BulkGetRequest(Collection<String> ids) {
        for (String id : ids) {
            this.getDocs().add(new BulkGetEntry(id));
        }
    }

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonIgnoreProperties(ignoreUnknown = true)
    @Data
    @NoArgsConstructor
    public static class BulkGetEntry {
        private String id;
        public BulkGetEntry(String id) {
            this.id = id;
        }
    }
}
