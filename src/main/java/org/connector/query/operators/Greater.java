package org.connector.query.operators;

import org.connector.query.CouchQuery;
import org.connector.query.operators.conditions.LongCondition;

/**
 * In the below example, we use an operator to match any document, where the "year" field has a value greater than 2010:
 * <p>
 * {
 * "year": {
 * "$gt": 2010
 * }
 * }
 */
public class Greater extends AbstractCondition {

    private LongCondition condition;

    public Greater(String field, long value) {
        setField(field);
        condition = new LongCondition("$gt", value);
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
