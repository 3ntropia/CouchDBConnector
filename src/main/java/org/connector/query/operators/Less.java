package org.connector.query.operators;

import org.connector.query.operators.conditions.LongCondition;

public class Less extends AbstractCondition {

    private final LongCondition condition;

    public Less(String field, long value) {
        setField(field);
        condition = new LongCondition("$lt", value);
    }

    public AbstractCondition getCondition() {
        return condition;
    }

    public String toString() {
        return "{\"" + getField() + "\":" + getCondition().toString() + "}";
    }
}
