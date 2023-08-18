package org.connector.dao.query;

import org.connector.http.Protocol;
import org.connector.impl.CouchDBClient;

public class CouchDBClientUtil {

    private static final String databaseUserName = "vV0gruPJYc6gN5QtCrrIAQ==";
    private static final String databasePassword = "vV0gruPJYc6gN5QtCrrIAQ==";
    private static final String databaseName = "marvel-integration-test";
    private static final Protocol protocol = Protocol.HTTP;
    private static final String databaseHost = "localhost";
    private static final Integer databasePort = 5984;

    public static CouchDBClient buildClient() {
        return CouchDBClient.builder()
                .url(protocol.getValue(), databaseHost, databasePort)
                .database(databaseName.toLowerCase())
                .username(databaseUserName)
                .password(databasePassword)
                .maxConnections(1)
                .testConnection(false)
                .build();
    }

}