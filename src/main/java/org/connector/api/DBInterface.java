package org.connector.api;

import org.connector.model.GetDatabaseInfoResponse;
import org.connector.model.PurgeResponse;
import org.connector.model.ViewRequest;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;

public interface DBInterface extends IndexInterface, PartitionInterface {

    int createDatabase();

    int createDatabase(@NotNull final String databaseName);

    int deleteDatabase();

    int deleteDatabase(@NotNull final String databaseName);

    boolean databaseExists(@NotNull final String databaseName);

    GetDatabaseInfoResponse getDatabaseInfo(@NotNull final String databaseName);

    PurgeResponse purge(String database, String id, Collection<String> revisions);

    boolean compact(String database);

    Boolean createView(ViewRequest viewRequest, String designDoc);

}
