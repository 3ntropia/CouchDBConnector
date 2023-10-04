package org.connector.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRawValue;
import lombok.Builder;
import org.springframework.lang.NonNull;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@Builder
public record FindRequest(
        @NonNull @JsonRawValue String selector,
        Integer limit,
        Integer skip,
        @JsonRawValue String sort,
        List<String> fields,
        @JsonProperty("use_index") List<String> indexes,
        String view,
        String filter,
        String bookmark) {
    private static Boolean conflicts = Boolean.FALSE;
}
