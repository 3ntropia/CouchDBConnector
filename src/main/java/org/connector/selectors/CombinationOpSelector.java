package org.connector.selectors;


import java.util.Collection;

public class CombinationOpSelector extends Selector {

    private CombinationOpSelector(CombinationOperator key, Object value) {
        super(key.getOp(), value);
    }

    private CombinationOpSelector(String field, CombinationOperator key, Object value) {
        super(field, new CombinationOpSelector(key, value));
    }

    public static CombinationOpSelector selectorsCombination(CombinationOperator operator, Collection<Selector> selectors) {
        return new CombinationOpSelector(operator, selectors);
    }

    public static CombinationOpSelector selectorCombination(CombinationOperator operator, Selector selector) {
        return new CombinationOpSelector(operator, selector);
    }

    public static CombinationOpSelector selectorCombination(String field, CombinationOperator operator, Selector selector) {
        return new CombinationOpSelector(field, operator, selector);
    }

    public static CombinationOpSelector valuesCombination(CombinationOperator operator, Collection<?> values) {
        return new CombinationOpSelector(operator, values);
    }

    public static CombinationOpSelector valuesCombination(String field, CombinationOperator operator, Collection<?> values) {
        return new CombinationOpSelector(field, operator, values);
    }

}