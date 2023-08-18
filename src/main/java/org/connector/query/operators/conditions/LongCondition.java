package org.connector.query.operators.conditions;

import org.connector.query.operators.AbstractCondition;

import java.util.Arrays;

public class LongCondition extends AbstractCondition {
    private long value;
    private long[] values = {};

    public LongCondition(String field, long value) {
        setField(field);
        this.value = value;
    }

    public LongCondition(String field, long[] values) {
        setField(field);
        this.values = values;
    }

    public String toString() {
        if (values.length == 0)
            return "{\"" + getField() + "\":" + value + "}";
        return "{\"" + getField() + "\":\"[" + Arrays.toString(values) + "]}";
    }
}
