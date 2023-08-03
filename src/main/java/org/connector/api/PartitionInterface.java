package org.connector.api;

import org.connector.model.GetPartitionResponse;
import org.jetbrains.annotations.NotNull;

import java.net.URI;

public interface PartitionInterface {


    GetPartitionResponse getPartitionInfo(String database, @NotNull String partition);

    @NotNull URI getPartitionURI(@NotNull String dbName, @NotNull String partition);
}
