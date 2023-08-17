package org.connector.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;

/**
 * Arguments
 *         newDoc – New version of document that will be stored.
 *         oldDoc – Previous version of document that is already stored.
 *         userCtx – User Context Object
 *         secObj – Security Object
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@Builder
public record ViewRequest(
        @JsonProperty("_id")
        String id,
        @JsonProperty("_rev") String revision,
        String language,
        String options,
        @JsonProperty("views") ViewName views,
        String filters,
        @JsonProperty("validate_doc_update") String validateUpdate,
        @JsonProperty("autoupdate") Boolean autoUpdate) {

}
