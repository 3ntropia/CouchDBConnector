package org.connector.api;

import org.connector.impl.CouchFindResult;
import org.connector.model.FindRequest;
import org.connector.query.CouchQuery;
import org.connector.model.Document;

import java.util.List;

public interface CouchDAOInterface<T extends Document> {

    T getById(String id);

    List<T> bulkGetByIds(List<String> ids);

    List<T> bulkGetByIds(String... ids);

    List<String> getNotDeletedIds(List<String> ids);

    T create(T o);

    List<T> create(List<T> toSave);

    T update(T o);

    List<T> update(List<T> toUpdate);

    void delete(String id);

    void bulkDelete(List<T> toDelete);

    <X extends Document> List<X> findBySubClass(FindRequest findRequest, Class<X> clazz);

    List<T> find(CouchQuery query);

    CouchFindResult<T> getCouchFindResult(FindRequest query, String partition);
}
