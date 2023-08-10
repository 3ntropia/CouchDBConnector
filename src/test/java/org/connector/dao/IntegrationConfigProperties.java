package org.connector.dao;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class IntegrationConfigProperties {
    private Boolean dbEnabled;
    private Boolean containerEnabled;
    private String dbHost;
    private Integer dbPort;
    private String dbUser;
    private String dbPass;
    private String dbName;
}
