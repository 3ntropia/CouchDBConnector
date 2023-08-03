package org.connector.query.operators.array;

import org.connector.query.CouchQuery;
import org.connector.query.operators.AbstractCondition;
import org.connector.query.operators.conditions.BooleanCondition;
import org.connector.query.operators.conditions.LongCondition;
import org.connector.query.operators.conditions.StringCondition;

/**
 * The $allMatch operator matches and returns all documents that contain an array field with all its elements matching
 * the supplied query criteria. Below is an example used with the primary index
 * <p>
 * {
 * "_id": { "$gt": null },
 * "genre": {
 * "$allMatch": {
 * "$eq": "Horror"
 * }
 * }
 * }
 */
public class AllMatch extends AbstractCondition {
    private AbstractCondition condition;


    public AllMatch(String field, String value) {
        setField(field);
        condition = new StringCondition("$eq", value);
    }

    public AllMatch(String field, boolean value) {
        setField(field);
        condition = new BooleanCondition("$eq", value);
    }

    public AllMatch(String field, int value) {
        setField(field);
        condition = new LongCondition("$eq", value);
    }

    protected <T extends CouchQuery> T track(T o) {
        if (o != null && o instanceof StringCondition) {
            condition = (StringCondition) o;
            o.setParent(this);
        }
        return o;
    }

    public String toString() {
        return "{\"" + getField() + "\": {\"$allMatch\":" + condition.toString() + "}}";
    }

}