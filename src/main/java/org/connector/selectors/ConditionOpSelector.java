package org.connector.selectors;

import lombok.Getter;

import java.util.Collection;

public class ConditionOpSelector extends Selector {

    private ConditionOpSelector(ConditionOperator operator, Object value) {
        super(operator, value);
    }

    private ConditionOpSelector(String field, Object value) {
        super(field, value);
    }

    private ConditionOpSelector(String field, ConditionOperator operator, Object value) {
        super(field, new ConditionOpSelector(operator, value));
    }

    public static ConditionOpSelector fieldValueCondition(String field, Object value) {
        return new ConditionOpSelector(field, value);
    }

    public static ConditionOpSelector valueCondition(ConditionOperator operator, Object value) {
        return new ConditionOpSelector(operator, value);
    }

    public static ConditionOpSelector valueCondition(String field, ConditionOperator operator, Object value) {
        return new ConditionOpSelector(field, operator, value);
    }

    public static ConditionOpSelector valuesCondition(ConditionOperator operator, Collection<?> values) {
        return new ConditionOpSelector(operator, values);
    }
    public static ConditionOpSelector valuesCondition(String field, ConditionOperator operator, Collection<?> values) {
        return new ConditionOpSelector(field, operator, values);
    }

    public static ConditionOpSelector booleanCondition(ConditionOperator operator, boolean value) {
        return new ConditionOpSelector(operator, value);
    }
    public static ConditionOpSelector booleanCondition(String field, ConditionOperator operator, boolean value) {
        return new ConditionOpSelector(field, operator, value);
    }

    public static ConditionOpSelector integerCondition(ConditionOperator operator, int value) {
        return new ConditionOpSelector(operator, value);
    }

    public static ConditionOpSelector integerCondition(String field, ConditionOperator operator, int value) {
        return new ConditionOpSelector(field, operator, value);
    }

    public static ConditionOpSelector stringCondition(ConditionOperator operator, String value) {
        return new ConditionOpSelector(operator, value);
    }

    public static ConditionOpSelector stringCondition(String field, ConditionOperator operator, String value) {
        return new ConditionOpSelector(field, operator, value);
    }

    public static ConditionOpSelector typeCondition(ConditionOperator operator, ValidType validType) {
        return new ConditionOpSelector(operator, validType.getType());
    }

    public static ConditionOpSelector typeCondition(String field, ConditionOperator operator, ValidType validType) {
        return new ConditionOpSelector(field, operator, validType.getType());
    }

    @Getter
    public enum ValidType {
        NULL("null"),
        BOOLEAN("boolean"),
        NUMBER("number"),
        STRING("string"),
        ARRAY("array"),
        OBJECT("object");
        private final String type;
        ValidType(String type) {
            this.type=type;
        }
    }
}
