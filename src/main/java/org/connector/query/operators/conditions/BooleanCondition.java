package org.connector.query.operators.conditions;

import org.connector.query.operators.AbstractCondition;

public class BooleanCondition extends AbstractCondition {
    private boolean value;

    public BooleanCondition(String field, boolean value) {
        setField(field);
        this.value = value;
    }

    public String toString() {
        return "{\"" + getField() + "\":" + value + "}";
    }
}
