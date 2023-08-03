package org.connector.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ViewRequest {
    @JsonProperty("_id")
    private String id;
    @JsonProperty("_rev")
    private String revision;
    private String language;
    private String options;
    @JsonProperty("views")
    private ViewName views;
    private String filters;
    /**
     * Arguments
     *         newDoc – New version of document that will be stored.
     *         oldDoc – Previous version of document that is already stored.
     *         userCtx – User Context Object
     *         secObj – Security Object
     */
    @JsonProperty("validate_doc_update")
    private String validateUpdate;
    @JsonProperty("autoupdate")
    private Boolean autoUpdate;
}
