package org.connector.dao;

import org.connector.http.Protocol;
import lombok.extern.slf4j.Slf4j;
import org.connector.impl.CouchDBClient;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.EnabledIfEnvironmentVariable;
import org.junit.jupiter.api.condition.EnabledIfEnvironmentVariables;

import static org.junit.jupiter.api.Assertions.assertNotNull;


@EnabledIfEnvironmentVariables({
        @EnabledIfEnvironmentVariable(named = "INTEGRATION_DB", matches = "true"),
        @EnabledIfEnvironmentVariable(named = "TEST_CONTAINER_DB", matches = "true")
})
class CouchDbClientTest extends AbstractCouchDbIntegrationTest {

    @Test
    void httpsClientConnectionTest() {
        /*
        var host = COUCH_DB.getHost();
        var port = COUCH_DB.getMappedPort(6984);

        var httpsClient = CouchDBClient.builder()
                .url(Protocol.HTTPS.getValue(), host, port)
                .database("httpsdatabase")
                .username(COUCH_DB.getEnvMap().get("COUCHDB_USER"))
                .password(COUCH_DB.getEnvMap().get("COUCHDB_PASSWORD"))
                .maxConnections(10)
                .initIndexesFromFiles(false)
                .createDatabase(false)
                .build();

        assertNotNull(httpsClient);

         */
    }

}
