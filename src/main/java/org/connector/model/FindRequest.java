package org.connector.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRawValue;
import lombok.*;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FindRequest {

    @NotNull
    @JsonRawValue
    private String selector;
    private Integer limit;
    private Integer skip;
    @JsonRawValue
    private String sort;
    private List<String> fields;
    @JsonProperty("use_index")
    private List<String> indexes;
    private String partition;
    private String view;
    private String filter;
    @Builder.Default
    private boolean conflicts = false;
    private String bookmark;

    public void addField(String field) {
        if (fields == null)
            fields = new ArrayList<>();
        fields.add(field);
    }

    public void addIndex(String index) {
        if (indexes == null)
            indexes = new ArrayList<>();
        indexes.add(index);
    }
}
