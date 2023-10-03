package org.connector.api;


import org.connector.http.CouchHttpHeaders;
import org.connector.model.*;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

import java.io.InputStream;
import java.util.Map;

public interface IDocuments {

    CouchHttpHeaders getDocumentInfo(@NonNull String docId);

    <T extends Document> T getDocumentById(@NonNull String docId, Class<T> clazz);
    <T extends Document> T getDocumentById(@NonNull String docId, boolean revs, Class<T> clazz);
    <T extends Document> T getDocumentByRev(@NonNull String docId, String rev, Class<T> clazz);
    <T extends Document> T getDocumentRevsInfo(@NonNull String docId, Class<T> clazz);

    <T extends Document> T getDocumentByIdWithAttachments(@NonNull String docId, boolean attachments,Class<T> clazz);

    <T extends Document> FindResponse<T> find(FindRequest request, String partition, Class<T> clazz);

    SaveResponse saveDocument(Document doc);
    SaveResponse saveDocument(@NonNull String docId, Document doc);

    SaveResponse saveDocument(@NonNull String docId, String rev, Document doc);

    SaveResponse saveAttachment(InputStream bytesIn, String name, String contentType, String docId, String rev);

    InputStream getAttachment(String name, String docId);

    InputStream getAttachment(String name, String docId, String rev);

    SaveResponse deleteDocument(@NonNull String docId, @NonNull String rev);

    CouchHttpHeaders getDesignDocHeads(String ddoc);

    CouchQueryViewResponse getDesignDocView(String ddoc, String view, @Nullable String partition,
                                            @Nullable Map<String,Object> queryParams);

    CouchQueryViewResponse findView(String partition, String designDocName, String viewName);

    CouchQueryViewResponse findView(String partition, String designDocName, String viewName, String key);

    CouchQueryViewResponse findView(String partition, String designDocName, String viewName, String starKey, String endKey);

}
