package org.connector.query;

import org.connector.impl.AbstractCouchDAO;
import org.connector.impl.CouchPaginator;
import org.connector.query.operators.Greater;
import org.connector.query.operators.Regex;
import org.connector.query.operators.Sort;
import org.connector.query.operators.array.AllMatch;
import org.connector.query.operators.array.ElementMatch;
import org.connector.query.operators.array.ElementMatchCollection;
import org.connector.query.operators.array.In;
import org.connector.query.operators.array.NotIn;
import org.connector.query.operators.conditions.BooleanCondition;
import org.connector.query.operators.conditions.LongCondition;
import org.connector.query.operators.conditions.StringCondition;
import org.connector.query.operators.GreaterEq;
import org.connector.query.operators.Less;
import org.connector.query.operators.LessEq;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public abstract class CouchQuery {
    private CouchQuery parent = null;
    private CouchPaginator paginator = null;
    private Sort sort;
    private String partition;
    private String view;
    private String filter;

    public static CollectionCondition.And _and() {
        return new CollectionCondition.And();
    }

    public CollectionCondition.And and() {
        return track(new CollectionCondition.And());
    }

    /**
     * The $or operator matches if any of the selectors in the array match. Below is an example used with an index on the field "year"
     *
     * @return
     */
    public static CollectionCondition.Or _or() {
        return new CollectionCondition.Or();
    }

    @Deprecated
    public static CouchQuery _elementMatch(String array, String field, String value) {
        return new ElementMatch(array).condition(field, value);
    }

    @Deprecated
    public static CouchQuery _elementMatch(String array, String value) {
        return new ElementMatch(array, value);
    }

    @Deprecated
    public static CouchQuery _elementMatch(String array) {
        return new ElementMatch(array);
    }

    @Deprecated
    public static StringCondition _notNullCondition(String field, String condition) {
        if (StringUtils.isNotBlank(field) && StringUtils.isNotBlank(condition))
            return new StringCondition(field, condition);
        return null;
    }

    @Deprecated
    public static StringCondition _condition(String field, String condition) {
        return new StringCondition(field, condition);
    }

    public <T extends AbstractCouchDAO<?>> CouchPaginator getPaginator(T dao) {
        if (paginator == null) {
            paginator = new CouchPaginator(this, dao);
        }
        return paginator;
    }

    public CollectionCondition.Or or() {
        return track(new CollectionCondition.Or());
    }

    @Deprecated
    public CouchQuery elementMatch(String array) {
        return track(new ElementMatch(array));
    }

    public CouchQuery elementMatch(String array, String value) {
        track(new ElementMatch(array, value));
        return this;
    }

    public CouchQuery allMatchRegex(String array, String field, String value) {
        track(new AllMatch(array, value).regex(field, value));
        return this;
    }

    public CouchQuery allMatch(String array, String field, String value) {
        track(new AllMatch(array, value).condition(field, value));
        return this;
    }

    public CouchQuery allMatch(String array, boolean value) {
        track(new AllMatch(array, value));
        return this;
    }

    public CouchQuery allMatch(String array, int value) {
        track(new AllMatch(array, value));
        return this;
    }

    public CouchQuery elementMatch(String array, String field, String value) {
        track(new ElementMatch(array).condition(field, value));
        return this;
    }

    public CouchQuery elementMatch(String array, String field, long value) {
        track(new ElementMatch(array).condition(field, value));
        return this;
    }

    public CouchQuery elementMatch(String array, String field, boolean value) {
        track(new ElementMatch(array).condition(field, value));
        return this;
    }

    public CouchQuery elementMatchRegex(String array, String field, String value) {
        track(new ElementMatch(array).regex(field, value));
        return this;
    }

    public CouchQuery elementMatch(String array, Map<String, String> mapFields) {
    	ElementMatchCollection emc = new ElementMatchCollection(array);
    	List<Object> conditions = new ArrayList<>();
    	for(Map.Entry<String, String> entrySet : mapFields.entrySet() ) {
    		conditions.add(new StringCondition(entrySet.getKey(), entrySet.getValue()));
    	}
    	emc.setConditions(conditions);
    	track(emc);
        return this;
    }

    public CouchQuery regex(String field, String value) {
        track(new Regex(field, value));
        return this;
    }

    public CouchQuery notNullCondition(String field, String value) {
        if (StringUtils.isNotBlank(field) && StringUtils.isNotBlank(value))
            track(new StringCondition(field, value));
        return this;
    }


    public CouchQuery less(String field, long value) {
        track(new Less(field, value));
        return this;
    }

    public CouchQuery lessEq(String field, long value) {
        track(new LessEq(field, value));
        return this;
    }

    public CouchQuery greaterEq(String field, long value) {
        track(new Greater(field, value));
        return this;
    }

    public CouchQuery greater(String field, long value) {
        track(new GreaterEq(field, value));
        return this;
    }

    public CouchQuery in(String field, String[] values) {
        track(new In(field, values));
        return this;
    }

    public CouchQuery notIn(String field, String[] values) {
        track(new NotIn(field, values));
        return this;
    }

    public CouchQuery condition(String field, String condition) {
        track(new StringCondition(field, condition));
        return this;
    }

    public CouchQuery condition(String field, long condition) {
        track(new LongCondition(field, condition));
        return this;
    }

    public CouchQuery condition(String field, boolean condition) {
        track(new BooleanCondition(field, condition));
        return this;
    }

    public CouchQuery sortBy(String field, Sort.Direction direction) {
        this.sort = new Sort(field, direction);
        return this;
    }

    public CouchQuery partition(String partition) {
        this.partition = partition;
        return this;
    }

    public CouchQuery view(String view) {
        this.view = view;
        return this;
    }

    public CouchQuery filter(String filter) {
        this.filter = filter;
        return this;
    }

    protected abstract <T extends CouchQuery> T track(T o);

    public CouchQuery getParent() {
        return parent;
    }

    public void setParent(CouchQuery parent) {
        this.parent = parent;
    }

    public Sort getSort() {
        return sort;
    }

    public void setSort(Sort sort) {
        this.sort = sort;
    }

    public CouchQuery top() {
        if (getParent() != null)
            return getParent().top();
        return this;
    }

    public String getPartition() {
        return partition;
    }

    public String getView() {
        return view;
    }

    public String getFilter() {
        return filter;
    }

}
