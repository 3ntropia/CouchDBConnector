package org.connector.api;

import org.connector.model.CreateIndexRequest;
import org.connector.model.CreateIndexResponse;
import org.connector.model.GetIndexResponse;
import org.apache.http.HttpResponse;
import org.springframework.lang.NonNull;

/**
 * https://docs.couchdb.org/en/latest/api/database/find.html#db-index
 */
interface IndexInterface {

    @NonNull
    CreateIndexResponse createIndex(@NonNull CreateIndexRequest index, String databaseName);

    @NonNull
    CreateIndexResponse createIndex(@NonNull String indexJsonString, String databaseName);

    @NonNull
    GetIndexResponse getIndexes(String database);

    HttpResponse deleteIndex(String database, @NonNull String ddoc, @NonNull String name);

    boolean indexExists(String indexName);
}
