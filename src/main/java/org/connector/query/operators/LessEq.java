package org.connector.query.operators;

import org.connector.query.CouchQuery;
import org.connector.query.operators.conditions.LongCondition;

public class LessEq extends AbstractCondition {

    private LongCondition condition;

    public LessEq(String field, long value) {
        setField(field);
        condition = new LongCondition("$lte", value);
    }

    public AbstractCondition getCondition() {
        return condition;
    }

    protected <T extends CouchQuery> T track(T o) {
        if (o instanceof LongCondition) {
            condition = (LongCondition) o;
            o.setParent(this);
        }
        return o;
    }

    public String toString() {
        return "{\"" + getField() + "\":" + getCondition().toString() + "}";
    }
}
