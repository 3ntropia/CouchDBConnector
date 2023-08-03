package org.connector.selectors;

import lombok.RequiredArgsConstructor;

import java.util.Collection;

@RequiredArgsConstructor
public class SelectorExpression {
    private final String field;

    public ConditionOpSelector is(Object value) {
        return ConditionOpSelector.fieldValueCondition(this.field, value);
    }

    public ConditionOpSelector lesserThan(Object value) {
        return ConditionOpSelector.valueCondition(field, ConditionOperator.LT, value);
    }

    public ConditionOpSelector lesserEqualsThan(Object value) {
        return ConditionOpSelector.valueCondition(field, ConditionOperator.LTE, value);
    }

    public ConditionOpSelector equalsTo(Object value) {
        return ConditionOpSelector.valueCondition(field, ConditionOperator.EQ, value);
    }

    public ConditionOpSelector notEqualsTo(Object value) {
        return ConditionOpSelector.valueCondition(field, ConditionOperator.NE, value);
    }

    public ConditionOpSelector greaterThan(Object value) {
        return ConditionOpSelector.valueCondition(field, ConditionOperator.GT, value);
    }

    public ConditionOpSelector greaterEqualsThan(Object value) {
        return ConditionOpSelector.valueCondition(field, ConditionOperator.GTE, value);
    }

    public ConditionOpSelector exists(Boolean exists) {
        return ConditionOpSelector.booleanCondition(field, ConditionOperator.EXISTS, exists);
    }

    public ConditionOpSelector type(ConditionOpSelector.ValidType validType) {
        return ConditionOpSelector.typeCondition(field, ConditionOperator.EXISTS, validType);
    }

    public ConditionOpSelector in(Collection<?> values) {
        return ConditionOpSelector.valuesCondition(this.field, ConditionOperator.IN, values);
    }

    public ConditionOpSelector notIn(Collection<?> values) {
        return ConditionOpSelector.valuesCondition(this.field, ConditionOperator.NIN, values);
    }

    public ConditionOpSelector size(Integer size) {
        return ConditionOpSelector.integerCondition(this.field, ConditionOperator.SIZE, size);
    }

    public ConditionOpSelector regex(String regex) {
        return ConditionOpSelector.stringCondition(this.field, ConditionOperator.REGEX, regex);
    }

    public CombinationOpSelector all(Collection<?> values) {
        return CombinationOpSelector.valuesCombination(this.field, CombinationOperator.ALL, values);
    }

    public CombinationOpSelector elemMatch(Selector selector) {
        return CombinationOpSelector.selectorCombination(field, CombinationOperator.ELEM_MATCH, selector);
    }

    public CombinationOpSelector allMatch(Selector selector) {
        return CombinationOpSelector.selectorCombination(field, CombinationOperator.ALL_MATCH, selector);
    }

    public CombinationOpSelector keyMapMatch(Selector selector) {
        return CombinationOpSelector.selectorCombination(field, CombinationOperator.KEY_MAP_MATCH, selector);
    }

}
