package org.connector.api;


import org.connector.http.CouchHttpHeaders;
import org.connector.model.SaveResponse;
import org.connector.model.Document;
import org.jetbrains.annotations.NotNull;

public interface DocumentInterface extends AllDocsInterface, BulkInterface, FindInterface, DesignDocsInterface {

    CouchHttpHeaders getDocumentInfo(@NotNull String docId);

    <T extends Document> Document getDocumentById(@NotNull String docId, Class<T> clazz);
    <T extends Document> Document getDocumentById(@NotNull String docId, boolean revs, Class<T> clazz);
    <T extends Document> Document getDocumentByRev(@NotNull String docId, String rev, Class<T> clazz);
    <T extends Document> Document getDocumentRevsInfo(@NotNull String docId, Class<T> clazz);

    SaveResponse saveDocument(Document doc);
    SaveResponse saveDocument(@NotNull String docId, Document doc);

    SaveResponse saveDocument(@NotNull String docId, String rev, Document doc);

    SaveResponse deleteDocument(@NotNull String docId, @NotNull String rev);

}
