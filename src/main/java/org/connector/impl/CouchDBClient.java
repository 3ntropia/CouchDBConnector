package org.connector.impl;

import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.protocol.HttpContext;
import org.connector.api.ICouchClient;
import org.connector.exceptions.CouchDBException;
import org.connector.http.CouchHttpHeaders;
import org.connector.model.*;
import org.connector.util.JSON;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.util.Assert;

import java.io.InputStream;
import java.net.URI;
import java.util.*;
import java.util.stream.Collectors;

import static org.apache.commons.lang3.StringUtils.isBlank;
import static org.apache.http.HttpStatus.SC_ACCEPTED;
import static org.connector.query.IntegrationConstants.*;
import static org.connector.util.JSON.toJson;

/**
 * Class used to interact with CouchDB. This client can be configured for both the connection URL details
 * and the number of allowable connections to that DB.
 * All methods here are a raw implementation of CouchDB most used APIs.
 * Interfaces are provided for encapsulation purposes.
 *
 */
public class CouchDBClient extends HTTPHandler implements ICouchClient {

    private final URI baseURI;
    private final String database;
    private final boolean isPartitioned;
    private final int bulkMaxSize; //todo use to implement in find requests.

    public CouchDBClient(@NonNull URI uri, @NonNull String database, @NonNull HttpHost host,
                         @NonNull HttpContext context, @NonNull HttpClient client, boolean createDatabase,
                         boolean defaultPartitioned, int bulkMaxSize, boolean testConnection) {
        super(host, context, client);
        Assert.notNull(uri, "URI (protocol, host, port) cannot be null");
        Assert.hasText(database, "database name must be provided.");
        Assert.notNull(host, "HTTP Host must not be null.");
        Assert.notNull(context, "Http Context must not be null");
        Assert.isTrue(10 <= bulkMaxSize && bulkMaxSize <= 100000,
                "BulkMaxSize must be greaterEq than 10 and lesserEq than 100000.");
        this.baseURI = uri;
        this.database = database;
        this.bulkMaxSize = bulkMaxSize;
        this.isPartitioned = defaultPartitioned;

        if(testConnection) {
            var response = getInstanceMetaInfo();
        }
        if (createDatabase && !databaseExists(database)){
            createDatabase();
        }
    }

    public static CouchDBClientBuilder builder() {
        return new CouchDBClientBuilder();
    }


    /**
     * Get root in order to retrieve basic Metadata Information for instance
     * GET /
     * @return JsonString representation of the response.
     */
    @Override
    public String getInstanceMetaInfo() {
        return get(baseURI, this::mapResponseToJsonString);
    }

    @Override
    public List<String> getAllDbs() {
        var rawJsonResponse = get(getURI(baseURI, COUCH_ALL_DBS_PATH), this::mapResponseToJsonString);
        return JSON.collectionFromJson(rawJsonResponse, List.class, String.class);
    }

    /**
     * Database API methods
     * @return
     */
    @Override
    public int deleteDatabase() {
        return deleteDatabase(database);
    }

    @Override
    public int deleteDatabase(@NonNull String databaseName) {
        return delete(getURI(baseURI, databaseName), r -> r.getStatusLine().getStatusCode());
    }

    @Override
    public int createDatabase() {
        return createDatabase(database);
    }

    @Override
    public int createDatabase(@NonNull String databaseName) {
        var dbURI = getURI(baseURI, databaseName);
        var uri = isPartitioned
                ? getURI(dbURI, List.of(asPair("partitioned", "true")))
                : dbURI;
        return put(uri, "", r -> r.getStatusLine().getStatusCode());
    }

    @Override
    public boolean databaseExists(@NonNull final String databaseName) {
        try {
            head(getURI(baseURI, databaseName));
        } catch (CouchDBException e){
            return false;
        }
        return true;
    }

    @Override
    public GetDatabaseInfoResponse getDatabaseInfo(@NonNull String databaseName) {
        var dbName = isBlank(databaseName) ? this.database : databaseName;
        return get(getURI(baseURI, dbName), response ->
                JSON.readValue(response.getEntity().getContent(), GetDatabaseInfoResponse.class));
    }

    @Override
    public PurgeResponse purge(String database, String id, Collection<String> revisions) {
        var dbName = isBlank(database) ? this.database : database;
        var body = toJson(Map.of(id, revisions));
        return post(getURI(baseURI, dbName, COUCH_PURGE_PATH), body, resp ->
                JSON.readValue(resp.getEntity().getContent(), PurgeResponse.class));
    }

    @Override
    public boolean compact(String database) {
        var dbName = isBlank(database) ? this.database : database;
        return post(getURI(baseURI, dbName, COUCH_COMPACT_PATH), "",
                resp -> resp.getStatusLine().getStatusCode() == SC_ACCEPTED);
    }

    @Override
    public GetAllDocsResponse getAllDocs(String partition) {
        URI uri;
        if (isPartitioned) {
            Assert.hasText(partition, "Partition cannot be null or empty");
            uri = getURI(getPartitionURI(database, partition), COUCH_ALL_DOCS_PATH);
        } else
            uri = getURI(baseURI, database, COUCH_ALL_DOCS_PATH);
        return get(uri, response ->
                JSON.readValue(response.getEntity().getContent(), GetAllDocsResponse.class));
    }

    @Override
    public GetAllDocsResponse postAllDocs(Collection<String> keys) {
        var uri = getURI(baseURI, database, COUCH_ALL_DOCS_PATH);
        return post(uri, toJson(keys), response ->
            JSON.readValue(response.getEntity().getContent(), GetAllDocsResponse.class));

    }

    @Override
    public @NonNull CreateIndexResponse createIndex(@NonNull CreateIndexRequest index, String databaseName) {
        return createIndex(toJson(index), databaseName);
    }

    @Override
    public @NonNull CreateIndexResponse createIndex(@NonNull String indexJsonString, String databaseName) {
        var dbName = isBlank(databaseName)? this.database : databaseName;
        var uri = getURI(baseURI, dbName, COUCH_INDEX_PATH);
        return post(uri, indexJsonString, response ->
                JSON.readValue(response.getEntity().getContent(), CreateIndexResponse.class));
    }

    /**
     * Create a design view
     * @param viewRequest
     * @param designDoc
     * @return 202 if created
     */
    public Boolean createView(ViewRequest viewRequest, String designDoc) {
        var uri = getURI(baseURI, database,"_design", designDoc);
        var code = put(uri, toJson(viewRequest), r -> r.getStatusLine().getStatusCode());
        return 201 == code || 202 == code;
    }

    public ViewRequest getView(String designDoc) {
        var uri = getURI(baseURI, database,"_design", designDoc);
        return get(uri, response -> JSON.readValue(response.getEntity().getContent(), ViewRequest.class));
    }

    @Override
    public @NonNull GetIndexResponse getIndexes(String database) {
        var dbName = isBlank(database)? this.database : database;
        var uri = getURI(baseURI, dbName, COUCH_INDEX_PATH);
        return get(uri, response -> JSON.readValue(response.getEntity().getContent(), GetIndexResponse.class));
    }

    @Override
    public HttpResponse deleteIndex(String database, @NonNull String ddoc, @NonNull String name) {
        var dbName = isBlank(database)? this.database : database;
        var uri = getURI(baseURI, dbName, COUCH_INDEX_PATH, ddoc,"json", name);
        return delete(uri, response -> response);
    }

    @Override
    public boolean indexExists(String indexName) {
        var response = getIndexes(database);
        return response.indexes().stream()
                .anyMatch(index -> index.name().equals(indexName));
    }

    @Override
    public GetPartitionResponse getPartitionInfo(String database, @NonNull String partition) {
        var dbName = isBlank(database)? this.database : database;
        var uri = getURI(baseURI, dbName, COUCH_PARTITION_PATH, partition);
        return get(uri, response -> JSON.readValue(response.getEntity().getContent(), GetPartitionResponse.class));
    }

    @Override
    public <T extends Document> FindResponse<T> find(FindRequest request, Class<T> clazz, String partition) {
        var uri = isBlank(partition)
                ? getURI(baseURI, database, COUCH_FIND_PATH)
                : getURI(baseURI, database, COUCH_PARTITION_PATH, partition, COUCH_FIND_PATH);
        var findType = JSON.getParameterizedType(FindResponse.class, clazz);
        // TODO: 12/7/22 handle pagination and max bulk get request value, handle overflow too
        var rawJsonResponse = post(uri, toJson(request), this::mapResponseToJsonString);
        return JSON.fromJson(rawJsonResponse, findType);
    }

    @Override
    public <T extends Document> BulkGetResponse<T> bulkGet(@NonNull BulkGetRequest request,@NonNull Class<T> clazz) {
        var uri = getURI(baseURI, database, COUCH_BULK_GET_PATH);
        var type = JSON.getParameterizedType(BulkGetResponse.class, clazz);
        var rawJsonResponse = post(uri, toJson(request), this::mapResponseToJsonString);
        return JSON.fromJson(rawJsonResponse, type);
    }

    @Override
    public <T extends Document> BulkSaveResponse bulkSave(@NonNull BulkSaveRequest<T> request) {
        var uri = getURI(baseURI, database, COUCH_BULK_DOCS_PATH);
        var typeReference = JSON.getCollectionType(List.class, BulkSaveResponse.BulkSaveResult.class);
        return post(uri, toJson(request), response ->
                new BulkSaveResponse(JSON.getJackson().readValue(response.getEntity().getContent(), typeReference)));
    }

    @Override
    public <T extends Document> BulkSaveResponse bulkDelete(@NonNull BulkSaveRequest<T> request) {
        request.docs().forEach(doc -> doc.setDeleted(true));
        return bulkSave(request);
    }

    @Override
    public <T extends Document> List<T> bulkGetByIds(@NonNull List<String> ids,@NonNull Class<T> clazz) {
        var bulkGetReq = new BulkGetRequest(ids);
        var response = bulkGet(bulkGetReq, clazz);
        if (response != null) {
            return response.getResults().stream()
                    .flatMap(result -> result.getDocs().stream())
                    .map(BulkGetResponse.BulkGetEntryDetail::getOk)
                    .collect(Collectors.toList());
        }
        return Collections.emptyList();
    }

    @Override
    public <T extends Document>  List<T> bulkGetByIds(@NonNull Class<T> clazz, String... ids) {
        return bulkGetByIds(Arrays.asList(ids), clazz);
    }

    @Override
    public CouchHttpHeaders getDesignDocHeads(String ddoc) {
        return head(getURI(baseURI, database, COUCH_DESIGN_DOC_PATH, ddoc));
    }

    @Override
    public CouchQueryViewResponse getDesignDocView(String ddoc, String view, @Nullable String partition,
                                                   @Nullable Map<String,Object> queryParams) {
        Assert.hasText(ddoc, "Design document name MUST be provided");
        Assert.hasText(view, "View name MUST be provided");
        var uri = isBlank(partition)
                ? getURI(baseURI, database, COUCH_DESIGN_DOC_PATH, ddoc, COUCH_VIEW_PATH, view)
                : getURI(baseURI, database, COUCH_PARTITION_PATH, partition, COUCH_DESIGN_DOC_PATH, ddoc, COUCH_VIEW_PATH, view);
        if (queryParams!=null && !queryParams.isEmpty()) {
            //CouchDB needs to see the double-quotes. (-_-)
            var paramPairs = queryParams.entrySet().stream()
                    .map(entry -> asPair(entry.getKey(), "\"" + entry.getValue().toString() + "\""))
                    .collect(Collectors.toList());
            uri = getURI(uri, paramPairs);
        }
        return get(uri, response -> JSON.readValue(response.getEntity().getContent(), CouchQueryViewResponse.class));
    }

    @Override
    public CouchHttpHeaders getDocumentInfo(@NonNull String docId) {
        return head(getURI(baseURI, database, docId));
    }

    @Override
    public <T extends Document> T getDocumentByIdWithAttachments(@NonNull String docId, boolean attachments,Class<T> clazz) {
        var uri = getURI(baseURI, List.of(database, docId), List.of(asPair("attachments", "true")));
        return getDocumentByURI(clazz, uri);
    }

    @Override
    public <T extends Document> T getDocumentById(@NonNull String docId, Class<T> clazz) {
        var uri = getURI(baseURI, database, docId);
        return getDocumentByURI(clazz, uri);
    }

    private <T extends Document> T getDocumentByURI(Class<T> clazz, URI uri) {
        var rawJsonResponse = get(uri, this::mapResponseToJsonString);
        return JSON.fromJson(rawJsonResponse, clazz);
    }

    @Override
    public <T extends Document> T getDocumentById(@NonNull String docId, boolean revs, Class<T> clazz) {
        var uri = getURI(baseURI, List.of(database, docId), List.of(asPair("revs", String.valueOf(revs))));
        return getDocumentByURI(clazz, uri);
    }

    @Override
    public <T extends Document> T getDocumentByRev(@NonNull String docId, String rev, Class<T> clazz) {
        var uri = getURI(baseURI, List.of(database, docId), List.of(asPair("rev", rev)));
        return getDocumentByURI(clazz, uri);
    }

    @Override
    public <T extends Document> T getDocumentRevsInfo(@NonNull String docId, Class<T> clazz) {
        var uri = getURI(baseURI, List.of(database, docId), List.of(asPair("revs_info", "true")));
        return getDocumentByURI(clazz, uri);
    }

    @Override
    public SaveResponse saveDocument(Document doc) {
        if (isBlank(doc.getId())) {
            var uri = getURI(baseURI, database);
            return post(uri, toJson(doc), response ->
                    JSON.readValue(response.getEntity().getContent(), SaveResponse.class));
        }
        return saveDocument(doc.getId(), doc);
    }

    @Override
    public SaveResponse saveDocument(@NonNull String docId, Document doc) {
        var uri = getURI(baseURI, database, docId);
        return put(uri, toJson(doc), response ->
                JSON.readValue(response.getEntity().getContent(), SaveResponse.class));
    }

    @Override
    public SaveResponse saveDocument(@NonNull String docId, String rev, Document doc) {
        var uri = getURI(baseURI, List.of(database, docId), List.of(asPair("rev", rev)));
        return put(uri, toJson(doc), response ->
                JSON.readValue(response.getEntity().getContent(), SaveResponse.class));
    }

    @Override
    public SaveResponse saveAttachment(InputStream bytesIn, String name, String contentType, String docId, String rev) {
        var uri = getURI(baseURI, List.of(database, docId, name), List.of(asPair("rev", rev)));
        return put(uri, bytesIn, contentType);
    }

    @Override
    public InputStream getAttachment(String name, String docId) {
        var uri = getURI(baseURI, database, docId, name);
        return get(uri);
    }

    @Override
    public InputStream getAttachment(String name, String docId, String rev) {
        var uri = getURI(baseURI, List.of(database, docId, name), List.of(asPair("rev", rev)));
        return get(uri);
    }

    @Override
    public SaveResponse deleteDocument(@NonNull String docId, @NonNull String rev) {
        var uri = getURI(baseURI, List.of(database, docId), List.of(asPair("rev", rev)));
        return delete(uri, response ->
                JSON.readValue(response.getEntity().getContent(), SaveResponse.class));
    }

    @Override
    public CouchQueryViewResponse findView(String partition, String ddoc, String view) {
        return getDesignDocView(ddoc, view, partition, null);
    }

    @Override
    public CouchQueryViewResponse findView(String partition, String designDocName, String viewName, String key) {
        return getDesignDocView(designDocName, viewName, partition, Map.of("key", key));
    }

    @Override
    public CouchQueryViewResponse findView(String partition, String designDocName, String viewName, String starKey, String endKey) {
        return getDesignDocView(designDocName, viewName, partition, Map.of("starKey", starKey, "endKey", endKey));
    }

    @Override
    public @NonNull URI getPartitionURI(@NonNull String dbName, @NonNull String partition) {
        return getURI(baseURI, dbName, COUCH_PARTITION_PATH, partition);
    }


}
