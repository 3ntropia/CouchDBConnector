package org.connector.impl;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CouchDBClientProperties {

    @Builder.Default
    private String url = "http://localhost:5984";
    @Builder.Default
    private String protocol = "http";
    @Builder.Default
    private String host = "localhost";
    @Builder.Default
    private Integer port = 5984;
    @Builder.Default
    private String username = "admin";
    @Builder.Default
    private String password = "admin";
    private String database;
    @Builder.Default
    private int bulkMaxSize = 100;
    @Builder.Default
    private int maxConnections = 10;
    @Builder.Default
    private boolean defaultPartitioned = true;
    @Builder.Default
    private boolean testConnection = true;
    @Builder.Default
    private boolean createDatabase = false;

    public void copy(CouchDBClientProperties properties) {
        this.url = properties.getUrl();
        this.protocol = properties.getProtocol();
        this.host = properties.getHost();
        this.port = properties.getPort();
        this.username = properties.getUsername();
        this.password = properties.getPassword();
        this.bulkMaxSize = properties.getBulkMaxSize();
        this.defaultPartitioned = properties.isDefaultPartitioned();
        this.database = properties.getDatabase();
        this.testConnection = properties.isTestConnection();
        this.createDatabase = properties.isCreateDatabase();
    }
}
