package org.connector.query.operators.array;

import org.connector.query.CouchQuery;
import org.connector.query.operators.AbstractCondition;
import org.connector.query.operators.conditions.StringCondition;

public class NotIn extends AbstractCondition {

    private StringCondition condition;

    public NotIn(String field) {
        setField(field);
    }

    public NotIn(String field, StringCondition condition) {
        setField(field);
        setCondition(condition);
    }

    public NotIn(String field, String[] values) {
        setField(field);
        setCondition(new StringCondition("$nin", values));
    }

    public StringCondition getCondition() {
        return condition;
    }

    public void setCondition(StringCondition condition) {
        this.condition = condition;
    }

    protected <T extends CouchQuery> T track(T o) {
        if (o instanceof StringCondition) {
            setCondition((StringCondition) o);
            o.setParent(this);
        }
        return o;
    }

    public String toString() {
        return "{\"" + getField() + "\": " + getCondition().toString() + "}";
    }
}
