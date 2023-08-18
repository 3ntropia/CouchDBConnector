package org.connector.query.operators.array;

import org.connector.query.CouchQuery;
import org.connector.query.operators.AbstractCondition;
import org.connector.query.operators.conditions.BooleanCondition;
import org.connector.query.operators.conditions.LongCondition;
import org.connector.query.operators.conditions.StringCondition;

/**
 * The $elemMatch operator matches and returns all documents that contain an array field with at least one element
 * matching the supplied query criteria. Below is an example used with the primary index (_all_docs)
 * {
 *   "_id": { "$gt": null },
 *   "genre": {
 *     "$elemMatch": {
 *       "$eq": "Horror"
 *     }
 *   }
 * }
 */
public class ElementMatch extends AbstractCondition {
    private AbstractCondition condition;

    public ElementMatch(String field) {
        setField(field);
    }

    public ElementMatch(String field, String value) {
        setField(field);
        condition = new StringCondition("$regex", value);
    }

    public ElementMatch(String field, boolean value) {
        setField(field);
        condition = new BooleanCondition("$eq", value);
    }

    public ElementMatch(String field, int value) {
        setField(field);
        condition = new LongCondition("$eq", value);
    }

    protected <T extends CouchQuery> T track(T o) {
        if (o != null && o instanceof AbstractCondition) {
            condition = (AbstractCondition) o;
            o.setParent(this);
        }
        return o;
    }

    public String toString() {
        return "{\"" + getField() + "\": {\"$elemMatch\":" + condition.toString() + "}}";
    }

}
