package org.connector.query.operators;

import org.connector.query.operators.conditions.StringCondition;

/**
 *
 */
public class Regex extends AbstractCondition {

    private StringCondition condition;

    public Regex(String field, String value) {
        setField(field);
        condition = new StringCondition("$regex", value);
    }

    public String toString() {
        return "{\"" + getField() + "\":" + condition.toString() + "}";
    }

}
