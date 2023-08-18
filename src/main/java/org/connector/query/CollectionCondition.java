package org.connector.query;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public abstract class CollectionCondition extends CouchQuery {
    private List<Object> conditions = new ArrayList<>();

    public CollectionCondition() {
    }

    public List<Object> getConditions() {
        return conditions;
    }

    public void setConditions(List<Object> conditions) {
        this.conditions = conditions;
    }

    //This logic has to move to the menu-service
    @Deprecated
    public CollectionCondition toPath(String path) {
        String[] pathes = path.split("/");
        String revPath = "";
        for (String p : pathes) {
            if (StringUtils.isNotBlank(p)) {
                revPath += ("/" + p);
                condition("document.path", revPath);
            }
        }
        return this;
    }

    public CollectionCondition all(String field, Collection<?> values) {
        for (Object value : values) {
            condition(field, value.toString());
        }
        return this;
    }

    protected <T extends CouchQuery> T track(T o) {
        getConditions().add(o);
        o.setParent(this);
        return o;
    }

    /**
     * The $and operator matches if all the selectors in the array match. Below is an example using the primary index (_all_docs):
     * {
     * "$and": [
     * {
     * "_id": { "$gt": null }
     * },
     * {
     * "year": {
     * "$in": [2014, 2015]
     * }
     * }
     * ]
     * }
     *
     * @return
     */
    public static class And extends CollectionCondition {

        public And() {
        }

        public String toString() {
            if (getConditions().size() > 1)
                return "{\"$and\" : [" + StringUtils.join(getConditions(), ",") + "]}";
            return getConditions().size() <= 0 ? "" : getConditions().get(0).toString();
        }
    }

    /**
     * The $or operator matches if any of the selectors in the array match. Below is an example used with an index on the field "year":
     * <p>
     * {
     * "year": 1977,
     * "$or": [
     * { "director": "George Lucas" },
     * { "director": "Steven Spielberg" }
     * ]
     * }
     */
    public static class Or extends CollectionCondition {

        public Or() {
        }

        public String toString() {
            if (getConditions().size() > 1)
                return "{\"$or\" : [" + StringUtils.join(getConditions(), ",") + "]}";
            return getConditions().size() <= 0 ? "" : getConditions().get(0).toString();
        }
    }
}
