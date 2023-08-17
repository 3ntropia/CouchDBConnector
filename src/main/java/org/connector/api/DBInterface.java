package org.connector.api;

import org.connector.model.*;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

import java.net.URI;
import java.util.Collection;
import java.util.List;

public interface DBInterface extends IndexInterface {

    int createDatabase();

    int createDatabase(@NonNull final String databaseName);

    int deleteDatabase();

    int deleteDatabase(@NonNull final String databaseName);

    boolean databaseExists(@NonNull final String databaseName);

    GetDatabaseInfoResponse getDatabaseInfo(@NonNull final String databaseName);

    PurgeResponse purge(String database, String id, Collection<String> revisions);

    boolean compact(String database);

    Boolean createView(ViewRequest viewRequest, String designDoc);

    String getInstanceMetaInfo();

    List<String> getAllDbs();

    GetPartitionResponse getPartitionInfo(String database, @NonNull String partition);

    @NonNull URI getPartitionURI(@NonNull String dbName, @NonNull String partition);

    GetAllDocsResponse getAllDocs(@Nullable String partition);

    GetAllDocsResponse postAllDocs(Collection<String> keys);
}
