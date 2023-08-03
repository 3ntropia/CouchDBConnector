package org.connector.query.operators.array;

import org.connector.query.CouchQuery;
import org.connector.query.operators.AbstractCondition;
import org.connector.query.operators.conditions.StringCondition;

public class In extends AbstractCondition {

    private StringCondition condition;

    public In(String field) {
        setField(field);
    }

    public In(String field, StringCondition condition) {
        setField(field);
        setCondition(condition);
    }

    public In(String field, String[] values) {
        setField(field);
        setCondition(new StringCondition("$in", values));
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
