package org.connector.selectors;

import lombok.Getter;

@Getter
public enum ConditionOperator implements Operator {
    LT("$lt"),
    LTE("$lte"),
    EQ("$eq"),
    NE("$ne"),
    GTE("$gte"),
    GT("$gt"),
    EXISTS("$exists"),
    TYPE("$type"),
    IN("$in"),
    NIN("$nin"),
    SIZE("$size"),
    MOD("$mod"),
    REGEX("$regex");

    private final String op;
    ConditionOperator(String op) {
        this.op=op;
    }
}
