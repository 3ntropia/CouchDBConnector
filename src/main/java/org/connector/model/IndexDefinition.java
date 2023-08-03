package org.connector.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.marvel.util.JSON;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class IndexDefinition {
    @Builder.Default
    private List<Object> fields = Collections.emptyList();
    @JsonProperty("partial_filter_selector")
    private Map<String,Object> selector;

    public boolean equalsToResponse(GetIndexResponse.IndexResponse indexResponse) {
        var fieldNames = indexResponse.getDef().getFields().stream()
                .map(JSON::convertToStringMap)
                .flatMap(stringStringMap -> stringStringMap.keySet().stream())
                .collect(Collectors.toList());
        return new HashSet<>(this.fields).containsAll(fieldNames);
    }
}
