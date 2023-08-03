package org.connector.selectors;

import lombok.Getter;

@Getter
public enum CombinationOperator implements Operator {
    AND("$and"),
    OR("$or"),
    NOT("$not"),
    NOR("$nor"),
    ALL("$all"),
    ELEM_MATCH("$elemMatch"),
    ALL_MATCH("$allMatch"),
    KEY_MAP_MATCH("$keyMapMatch");

    private final String op;
    CombinationOperator(String op) {
        this.op=op;
    }
}
