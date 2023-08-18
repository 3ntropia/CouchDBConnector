package org.connector.query.operators;

import org.connector.query.CouchQuery;
import org.connector.query.operators.conditions.LongCondition;

public class GreaterEq extends AbstractCondition {

    private LongCondition condition;

    public GreaterEq(String field, long value) {
        setField(field);
        condition = new LongCondition("$gte", value);
    }

    public LongCondition getCondition() {
        return condition;
    }

    protected <T extends CouchQuery> T track(T o) {
        if (o instanceof LongCondition) {
            condition = ((LongCondition) o);
            o.setParent(this);
        }
        return o;
    }

    public String toString() {
        return "{\"" + getField() + "\":" + getCondition().toString() + "}";
    }
}
