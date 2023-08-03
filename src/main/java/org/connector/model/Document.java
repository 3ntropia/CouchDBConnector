package org.connector.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Abstract superclass of all persistable objects in Marvel.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@SuperBuilder(toBuilder = true)
@ToString
@Getter
@Setter
@NoArgsConstructor
public class Document {
    @JsonProperty("_id")
    private String id;
    @JsonProperty("_rev")
    private String rev;
    @JsonProperty("_deleted")
    private boolean deleted = false;
    private String type;
    @JsonProperty("_attachments")
    private Map<String, Attachment> attachments;
    @JsonProperty("_revisions")
    private DocumentRevisions revisions;
    @JsonProperty("_revs_info")
    private List<RevisionInfo> revisionInfos = new ArrayList<>();


    @Data
    @NoArgsConstructor
    public static class DocumentRevisions {
        private int start;
        private List<String> ids;
    }

    @Data
    @NoArgsConstructor
    public static class RevisionInfo {
        private String rev;
        private String status;
    }
}
