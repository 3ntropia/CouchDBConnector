package org.connector.api;

import org.connector.model.CouchFindResult;
import org.connector.model.Document;
import org.connector.model.FindRequest;
import org.connector.query.CouchQuery;

import java.util.List;

public interface ICouchDAO<T extends Document> {

    T getById(String id);

    List<T> getByIds(List<String> ids);

    T create(T o);

    List<T> create(List<T> toSave);

    T update(T o);

    List<T> update(List<T> toUpdate);

    boolean delete(String id);

    boolean delete(List<String> id);

    <X extends Document> List<X> findBySubClass(FindRequest findRequest, Class<X> clazz, String partition);

    List<T> find(CouchQuery query);

    CouchFindResult<T> getCouchFindResult(FindRequest query, String partition);
}
