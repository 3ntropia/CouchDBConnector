package org.connector.impl;

import org.connector.model.CouchFindResult;
import org.connector.model.Document;
import org.connector.query.CouchQuery;

import java.util.ArrayList;
import java.util.List;

public class CouchPaginator<T extends Document> {
    private final AbstractCouchDAO<T> dao;
    private final CouchQuery query;
    private int pageSize = 50;
    private String bookmark;
    private int page = 0;

    public CouchPaginator(CouchQuery query, AbstractCouchDAO<T> dao) {
        this.query = query;
        this.dao = dao;
    }

    public CouchPaginator(CouchQuery query, AbstractCouchDAO<T> dao, int pageSize) {
        this(query, dao);
        this.pageSize = pageSize;
    }

    public List<T> next() {
        CouchFindResult<T> results = dao.getCouchFindResult(query, pageSize, bookmark);
        this.bookmark = results.bookmark();
        return results.results();
    }

    public List<T> getPage(int page) {
        CouchFindResult<T> results = dao.getCouchFindResult(query, pageSize, bookmark, page);
        this.bookmark = results.bookmark();
        return results.results();
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public List<T> all() {
        int oldPageSize = pageSize;
        pageSize = 1000;
        try {
            List<T> results = new ArrayList<>();
            List<T> page;
            while (!(page = next()).isEmpty()) {
                results.addAll(page);
            }
            return results;
        } finally {
            pageSize = oldPageSize;
        }
    }
}
