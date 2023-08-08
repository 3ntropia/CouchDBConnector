package org.connector.api;


import org.connector.http.CouchHttpHeaders;
import org.connector.model.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Map;

public interface DocumentInterface <T extends Document> {

    CouchHttpHeaders getDocumentInfo(@NotNull String docId);

    T getDocumentById(@NotNull String docId, Class<T> clazz);
    <T extends Document> Document getDocumentById(@NotNull String docId, boolean revs, Class<T> clazz);
    <T extends Document> Document getDocumentByRev(@NotNull String docId, String rev, Class<T> clazz);
    <T extends Document> Document getDocumentRevsInfo(@NotNull String docId, Class<T> clazz);

    <T extends Document> FindResponse<T> find(FindRequest request, String partition, Class<T> clazz);

    SaveResponse saveDocument(Document doc);
    SaveResponse saveDocument(@NotNull String docId, Document doc);

    SaveResponse saveDocument(@NotNull String docId, String rev, Document doc);

    SaveResponse deleteDocument(@NotNull String docId, @NotNull String rev);

    <T extends Document> BulkGetResponse<T> bulkGet(BulkGetRequest request, Class<T> clazz);

    <T extends Document> BulkSaveResponse bulkSave(@NotNull BulkSaveRequest<T> request);

    <T extends Document> BulkSaveResponse bulkDelete(@NotNull BulkSaveRequest<T> request);

    CouchHttpHeaders getDesignDocHeads(String ddoc);

    CouchQueryViewResponse getDesignDocView(String ddoc, String view, @Nullable String partition,
                                            @Nullable Map<String,Object> queryParams);

    CouchQueryViewResponse findView(String partition, String designDocName, String viewName);

    CouchQueryViewResponse findView(String partition, String designDocName, String viewName, String key);

    CouchQueryViewResponse findView(String partition, String designDocName, String viewName, String starKey, String endKey);
}
