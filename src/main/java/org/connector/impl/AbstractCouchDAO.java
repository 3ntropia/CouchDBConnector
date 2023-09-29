package org.connector.impl;

import org.connector.api.ICouchDAO;
import org.connector.api.IDocuments;
import org.connector.model.BulkGetRequest;
import org.connector.model.BulkGetResponse;
import org.connector.model.BulkSaveRequest;
import org.connector.model.CouchFindResult;
import org.connector.model.Document;
import org.connector.model.FindRequest;
import org.connector.model.FindResponse;
import org.connector.query.CouchQuery;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Generic DAO superclass for a DAO when using CouchDB.
 *
 * @param <T> - the type of the items to be persisted in this DAO
 */
public abstract class AbstractCouchDAO <T extends Document> implements ICouchDAO<T> {

    /**
     * Type used for deserialization. Built once and cached per DAO
     */
    //private final JavaType type;

    /**
     * The CouchDB client class that interacts over HTTP(S) with CouchDB
     */
    protected final CouchDBClient client;
    protected final Class<T> entityClass;
    protected final String entityType;

    protected AbstractCouchDAO(CouchDBClient client, Class<T> entityClass) {
        this.client = client;
        this.entityClass = entityClass;
        this.entityType = entityClass.getSimpleName();
    }

    /**
     * Gets the CouchDB client in case there's a need for low level requests outside this DAO.
     *
     * @return
     */
    public CouchDBClient getClient() {
        return (CouchDBClient)client;
    }

    @Override
    public T getById(String id) {
        return client.getDocumentById(id, entityClass);
    }

    @Override
    public T create(T o) {
        var result = client.saveDocument(o.getId(), o);
        o.setRev(result.rev());
        o.setId(result.id());
        return o;
    }

    @Override
    public List<T> create(List<T> toSave) {
        var result = client.bulkSave(new BulkSaveRequest<>(toSave));
        return toSave;
    }

    @Override
    public T update(T o) {
        var result = client.saveDocument(o);
        o.setRev(result.rev());
        return o;
    }

    @Override
    public List<T> update(List<T> toUpdate) {
        var mapUpdate = toUpdate.stream()
                .collect(Collectors.toMap(Document::getId, x -> x));
        var fromDb = client.bulkGet(new BulkGetRequest(mapUpdate.keySet()), this.entityClass);
        var toSave = fromDb.getResults().stream()
                .flatMap(entry -> entry.getDocs().stream())
                .map(BulkGetResponse.BulkGetEntryDetail::getOk)
                //.peek(x->x.setDocument(mapUpdate.get(x.getId())))
                .collect(Collectors.toList());
        var result = client.bulkSave(new BulkSaveRequest<>(toSave));
        return toUpdate;
    }

    @Override
    public void delete(String id) {
        var toDelete = client.getDocumentById(id, this.entityClass);
        var result = client.deleteDocument(toDelete.getId(), toDelete.getRev());
    }

    /**
     * Fetch a certain subclass, lets say some specific fields of the document and map into a new class
     * This could be handy to fetch smaller objects. Ie document.id.
     * As all documents saved in couch by this DAO extend from AbstractPersistable, you may consider using inner classes
     * for some requests related to the minimal fields you are expecting.
     */
    @Override
    public <X extends Document> List<X> findBySubClass(FindRequest findRequest, Class<X> clazz) {
        var findResponse = this.client.find(findRequest, "", clazz);
        return new ArrayList<>(findResponse.docs());
    }

    @Override
    public List<T> find(CouchQuery query) {
        return query.getPaginator(this).all();
    }

    @Override
    public CouchFindResult<T> getCouchFindResult(FindRequest query, String partition) {
        var response = this.client.find(query, partition, entityClass);
        return new CouchFindResult<>(unwrapFindResponse(response), response.bookmark());
    }

    CouchFindResult<T> getCouchFindResult(CouchQuery c, int pageSize, String bookmark) {
        return getCouchFindResult(FindRequest.builder()
                .selector(c.toString())
                .limit(pageSize)
                .bookmark(bookmark)
                .build(), c.getPartition());
    }

    CouchFindResult<T> getCouchFindResult(CouchQuery c, int pageSize, String bookmark, int page) {
        return getCouchFindResult(FindRequest.builder()
                .selector(c.toString())
                .limit(pageSize)
                .skip(pageSize * page)
                .bookmark(bookmark)
                .build(), c.getPartition());
    }

    protected List<T> unwrapFindResponse(FindResponse<T> findResponse) {
        return new ArrayList<>(findResponse.docs());
    }

}
