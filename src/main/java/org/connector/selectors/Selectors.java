package org.connector.selectors;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.List;


@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public interface Selectors {

    static SelectorExpression field(String field) {
        return new SelectorExpression(field);
    }

    static ConditionOpSelector fieldEq(String field, Object value) {
        return ConditionOpSelector.fieldValueCondition(field, value);
    }

    static CombinationOpSelector and(Selector... selectors) {
        return and(List.of(selectors));
    }
    
    static CombinationOpSelector and(List<Selector> selectors) {
        return CombinationOpSelector.selectorsCombination(CombinationOperator.AND, selectors);
    }

    static CombinationOpSelector or(Selector... selectors) {
        return or(List.of(selectors));
    }

    static CombinationOpSelector or(List<Selector> selectors) {
        return CombinationOpSelector.selectorsCombination(CombinationOperator.OR, selectors);
    }

    static CombinationOpSelector nor(Selector... selectors) {
        return nor(List.of(selectors));
    }

    static CombinationOpSelector nor(List<Selector> selectors) {
        return CombinationOpSelector.selectorsCombination(CombinationOperator.NOR, selectors);
    }

    static CombinationOpSelector not(Selector selector) {
        return CombinationOpSelector.selectorCombination(CombinationOperator.NOT, selector);
    }

    static ConditionOpSelector lesserThan(Object value) {
        return ConditionOpSelector.valueCondition(ConditionOperator.LT, value);
    }

    static ConditionOpSelector lesserEqualsThan(Object value) {
        return ConditionOpSelector.valueCondition(ConditionOperator.LTE, value);
    }

    static ConditionOpSelector equalsTo(Object value) {
        return ConditionOpSelector.valueCondition(ConditionOperator.EQ, value);
    }

    static ConditionOpSelector notEqualsTo(Object value) {
        return ConditionOpSelector.valueCondition(ConditionOperator.NE, value);
    }

    static ConditionOpSelector greaterThan(Object value) {
        return ConditionOpSelector.valueCondition(ConditionOperator.GT, value);
    }

    static ConditionOpSelector greaterEqualsThan(Object value) {
        return ConditionOpSelector.valueCondition(ConditionOperator.GTE, value);
    }


}
