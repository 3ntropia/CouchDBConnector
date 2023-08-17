package org.connector.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class CouchQueryResponse {
    private String docs;
    private String bookmark;
    private String warning;

    public CouchQueryResponse() {
    }

    public String getDocs() {
        return docs;
    }

    public void setDocs(String docs) {
        this.docs = docs;
    }

    public String getBookmark() {
        return bookmark;
    }

    public void setBookmark(String bookmark) {
        this.bookmark = bookmark;
    }

    public String getWarning() {
        return warning;
    }

    public void setWarning(String warning) {
        this.warning = warning;
    }
}
