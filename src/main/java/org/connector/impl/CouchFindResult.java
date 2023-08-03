package org.connector.impl;

import org.connector.model.Document;

import java.util.List;

public class CouchFindResult<X extends Document> {
    private final List<X> results;
    private final String bookmark;

    public CouchFindResult(List<X> results, String bookmark) {
        this.results = results;
        this.bookmark = bookmark;
    }

    public List<X> getResults() {
        return results;
    }

    public String getBookmark() {
        return bookmark;
    }

}