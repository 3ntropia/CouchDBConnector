package org.connector.impl;

import org.connector.api.DocumentInterface;
import org.connector.api.CouchDAOInterface;
import org.connector.model.CouchFindResult;
import org.connector.query.CouchQuery;
import org.connector.model.Document;
import org.connector.model.BulkGetRequest;
import org.connector.model.BulkGetResponse;
import org.connector.model.BulkSaveRequest;
import org.connector.model.FindRequest;
import org.connector.model.FindResponse;

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
public abstract class AbstractCouchDAO <T extends Document> implements CouchDAOInterface <T> {

    /**
     * Type used for deserialization. Built once and cached per DAO
     */
    //private final JavaType type;

    /**
     * The CouchDB client class that interacts over HTTP(S) with CouchDB
     */
    protected final DocumentInterface client;
    protected final Class<T> entityClass;
    protected final String entityType;

    protected AbstractCouchDAO(CouchDBClient client, Class<T> entityClass) {
        this.client = client;
        this.entityClass = entityClass;
        this.entityType = entityClass.getSimpleName();
        //this.type = JSON.getParameterizedType(Document.class, entityClass);
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
    public T getById(String id)  {
         return this.client.getDocumentById(id, entityClass);
    }

    @Override
    public List<T> bulkGetByIds(List<String> ids) {
        var bulkGetReq = new BulkGetRequest(ids);
        var response = this.client.bulkGet(bulkGetReq, this.entityClass);
        if (response != null) {
            return response.getResults().stream()
                    .flatMap(result -> result.getDocs().stream())
                    .map(BulkGetResponse.BulkGetEntryDetail::getOk)
                    .collect(Collectors.toList());
        }
        return Collections.emptyList();
    }
    
    @Override
    public List<T> bulkGetByIds(String... ids) {
        return bulkGetByIds(Arrays.asList(ids));
    }

    @Override
    public List<String> getNotDeletedIds(List<String> ids) {
        return this.bulkGetByIds(ids).stream()
                .map(Document::getId)
                .collect(Collectors.toList());
    }

    @Override
    public T create(T o) {
        var result = this.client.saveDocument(o.getId(), o);
        return o;
    }

    @Override
    public List<T> create(List<T> toSave) {
        var result = this.client.bulkSave(new BulkSaveRequest<>(toSave));
        return toSave;
    }

    @Override
    public T update(T o) {
        var result = this.client.saveDocument(o);
        return o;
    }

    @Override
    public List<T> update(List<T> toUpdate) {
        var mapUpdate = toUpdate.stream()
                .collect(Collectors.toMap(Document::getId, x -> x));
        var fromDb = this.client.bulkGet(new BulkGetRequest(mapUpdate.keySet()), this.entityClass);
        var toSave = fromDb.getResults().stream()
                .flatMap(entry -> entry.getDocs().stream())
                .map(BulkGetResponse.BulkGetEntryDetail::getOk)
                //.peek(x->x.setDocument(mapUpdate.get(x.getId())))
                .collect(Collectors.toList());
        var result = this.client.bulkSave(new BulkSaveRequest<>(toSave));
        return toUpdate;
    }

    @Override
    public void delete(String id) {
        var toDelete = this.client.getDocumentById(id, this.entityClass);
        var result = this.client.deleteDocument(toDelete.getId(), toDelete.getRev());
    }

    @Override
    public void bulkDelete(List<T> toDelete) {
        var idsDelete = toDelete.stream().map(Document::getId).collect(Collectors.toList());
        var fromDb = this.client.bulkGet(new BulkGetRequest(idsDelete), this.entityClass);
        var toSave = fromDb.getResults().stream()
                .flatMap(entry -> entry.getDocs().stream())
                .map(BulkGetResponse.BulkGetEntryDetail::getOk)
                .collect(Collectors.toList());
        var result = this.client.bulkDelete(new BulkSaveRequest<>(toSave));
    }


    /**
     * In case you want to fetch a certain subclass, lets say some specific fields of the document and map into a new class
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
