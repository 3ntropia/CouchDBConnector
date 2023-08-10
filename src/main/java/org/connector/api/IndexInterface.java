package org.connector.api;

import org.connector.model.CreateIndexRequest;
import org.connector.model.CreateIndexResponse;
import org.connector.model.GetIndexResponse;
import org.apache.http.HttpResponse;
import org.jetbrains.annotations.NotNull;

/**
 * https://docs.couchdb.org/en/latest/api/database/find.html#db-index
 */
interface IndexInterface {

    @NotNull
    CreateIndexResponse createIndex(@NotNull CreateIndexRequest index, String databaseName);

    @NotNull
    CreateIndexResponse createIndex(@NotNull String indexJsonString, String databaseName);

    @NotNull
    GetIndexResponse getIndexes(String database);

    HttpResponse deleteIndex(String database, @NotNull String ddoc, @NotNull String name);

    boolean indexExists(String indexName);
}
