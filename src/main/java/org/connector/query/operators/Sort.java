package org.connector.query.operators;

import org.connector.query.operators.conditions.StringCondition;

/**
 * The sort field contains a list of field name and direction pairs, expressed as a basic array.
 * The first field name and direction pair is the topmost level of sort. The second pair, if provided, is the next level of sort.
 * <p>
 * [{"fieldName1": "desc"}, {"fieldName2": "desc" }]
 * <p>
 * In order to sort you will need to create the index in the db.
 */
public class Sort extends StringCondition {

    public enum Direction {
        ASC("asc"), DESC("desc");

        public String direction;

        Direction(String direction) {
            this.direction = direction;
        }

        public String getValue() {
            return direction;
        }
    }

    private final Direction direction;

    public Sort(String field, Direction direction) {
        super(field);
        this.direction = direction;
    }

    public String toString() {
        return "[{\"" + getValue() + "\":\"" + direction.getValue() + "\"}]";

    }
}
