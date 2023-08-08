package org.connector.api;

import org.connector.impl.CouchFindResult;
import org.connector.model.*;
import org.jetbrains.annotations.NotNull;
import org.springframework.lang.Nullable;

import java.net.URI;
import java.util.Collection;
import java.util.List;

public interface DBInterface extends IndexInterface {

    int createDatabase();

    int createDatabase(@NotNull final String databaseName);

    int deleteDatabase();

    int deleteDatabase(@NotNull final String databaseName);

    boolean databaseExists(@NotNull final String databaseName);

    GetDatabaseInfoResponse getDatabaseInfo(@NotNull final String databaseName);

    PurgeResponse purge(String database, String id, Collection<String> revisions);

    boolean compact(String database);

    Boolean createView(ViewRequest viewRequest, String designDoc);

    String getInstanceMetaInfo();

    List<String> getAllDbs();

    GetPartitionResponse getPartitionInfo(String database, @NotNull String partition);

    @NotNull URI getPartitionURI(@NotNull String dbName, @NotNull String partition);

    GetAllDocsResponse getAllDocs(@Nullable String partition);

    GetAllDocsResponse postAllDocs(Collection<String> keys);
}
