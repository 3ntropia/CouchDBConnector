package org.connector.query.operators.conditions;

import org.connector.query.operators.AbstractCondition;

import java.util.Arrays;
import java.util.stream.Collectors;

public class StringCondition extends AbstractCondition {
    private String value;
    private String[] values = {};

    public StringCondition(String value) {
        this.value = value;
    }

    public StringCondition(String field, String value) {
        setField(field);
        this.value = value;
    }

    public StringCondition(String field, String[] values) {
        setField(field);
        this.values = values;
    }

    public String[] getValues() {
        return values;
    }

    public String getValue() {
        return value;
    }

    public String toString() {
        if (values.length == 0)
            return "{\"" + getField() + "\":\"" + value + "\"}";
        return "{\"" + getField() + "\":[" + Arrays.stream(values).map(s -> "\"" + s + "\"") .collect(Collectors.joining(", ")) + "]}";
    }

}
