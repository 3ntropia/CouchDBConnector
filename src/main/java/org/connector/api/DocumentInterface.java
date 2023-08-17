package org.connector.api;


import org.connector.http.CouchHttpHeaders;
import org.connector.model.*;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

import java.util.Map;

public interface DocumentInterface {

    CouchHttpHeaders getDocumentInfo(@NonNull String docId);

    <T extends Document> T getDocumentById(@NonNull String docId, Class<T> clazz);
    <T extends Document> T getDocumentById(@NonNull String docId, boolean revs, Class<T> clazz);
    <T extends Document> T getDocumentByRev(@NonNull String docId, String rev, Class<T> clazz);
    <T extends Document> T getDocumentRevsInfo(@NonNull String docId, Class<T> clazz);

    <T extends Document> FindResponse<T> find(FindRequest request, String partition, Class<T> clazz);

    SaveResponse saveDocument(Document doc);
    SaveResponse saveDocument(@NonNull String docId, Document doc);

    SaveResponse saveDocument(@NonNull String docId, String rev, Document doc);

    SaveResponse deleteDocument(@NonNull String docId, @NonNull String rev);

    <T extends Document> BulkGetResponse<T> bulkGet(BulkGetRequest request, Class<T> clazz);

    <T extends Document> BulkSaveResponse bulkSave(@NonNull BulkSaveRequest<T> request);

    <T extends Document> BulkSaveResponse bulkDelete(@NonNull BulkSaveRequest<T> request);

    CouchHttpHeaders getDesignDocHeads(String ddoc);

    CouchQueryViewResponse getDesignDocView(String ddoc, String view, @Nullable String partition,
                                            @Nullable Map<String,Object> queryParams);

    CouchQueryViewResponse findView(String partition, String designDocName, String viewName);

    CouchQueryViewResponse findView(String partition, String designDocName, String viewName, String key);

    CouchQueryViewResponse findView(String partition, String designDocName, String viewName, String starKey, String endKey);
}
