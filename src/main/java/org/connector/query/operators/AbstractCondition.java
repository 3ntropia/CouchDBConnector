package org.connector.query.operators;

import org.connector.query.CouchQuery;

public abstract class AbstractCondition extends CouchQuery {
    private String field;

    public String getField() {
        return field;
    }

    public void setField(String field) {
        this.field = field;
    }

    protected <T extends CouchQuery> T track(T o) {
        return o;
    }

}