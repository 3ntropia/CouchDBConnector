package org.connector.dao;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.connector.http.Protocol;
import org.connector.impl.AbstractCouchDAO;
import org.connector.impl.CouchDBClient;
import org.connector.dao.query.entities.SomeDAO;
import org.connector.model.Document;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import lombok.extern.slf4j.Slf4j;
import org.connector.util.JSON;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.utility.DockerImageName;
import org.testcontainers.utility.MountableFile;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

@Slf4j
public abstract class AbstractCouchDbIntegrationTest {

    static final String integrationTestEnabled = System.getenv("INTEGRATION_DB");
    static final String testContainerEnabled = System.getenv("TEST_CONTAINER_DB");
    static final Function<List<? extends Document>, List<String>> toIds =
            couchEntities -> couchEntities.stream()
                    .map(Document::getId)
                    .collect(Collectors.toList());

    static GenericContainer<?> COUCH_DB;
    static CouchDBClient couchDbClient;
    static ParentDAO parentDAO;
    static ChildDAO childDAO;
    static SomeDAO someDAO;

    @BeforeAll
    static void setUpDatabaseClient() throws Exception {
        if (integrationTestEnabled.equals("true")) {
            var dbHost = System.getenv("COUCH_DB_HOST");
            var dbPort = Integer.parseInt(System.getenv("COUCH_DB_PORT"));
            var dbUser = System.getenv("COUCH_DB_USER");
            var dbPass = System.getenv("COUCH_DB_PASS");
            var dbName = System.getenv("COUCH_DB_NAME");

            if (testContainerEnabled.equals("true")) {
                COUCH_DB = new GenericContainer<>(DockerImageName.parse("couchdb:latest"))
                        .withExposedPorts(5984, 6984)
                        .withEnv("COUCHDB_USER", dbUser)
                        .withEnv("COUCHDB_PASSWORD", dbPass)
                        .withCopyFileToContainer(MountableFile.forClasspathResource("couchdb/local.ini", 0744),"/opt/couchdb/etc/local.ini")
                        .withCopyFileToContainer(MountableFile.forClasspathResource("couchdb/server.crt", 0744),"/opt/couchdb/etc/server.crt")
                        .withCopyFileToContainer(MountableFile.forClasspathResource("couchdb/server.csr", 0744),"/opt/couchdb/etc/server.csr")
                        .withCopyFileToContainer(MountableFile.forClasspathResource("couchdb/server.key", 0744),"/opt/couchdb/etc/server.key");

                COUCH_DB.start();
                dbHost = COUCH_DB.getHost();
                dbPort = COUCH_DB.getMappedPort(5984);
            }

            couchDbClient = CouchDBClient.builder()
                    .url(Protocol.HTTP.getValue(), dbHost, dbPort)
                    .database(dbName)
                    .username(dbUser)
                    .password(dbPass)
                    .maxConnections(50)
                    .createDatabase(true)
                    .build();
            childDAO = new ChildDAO(couchDbClient);
            parentDAO = new ParentDAO(couchDbClient);
            someDAO = new SomeDAO(couchDbClient);
        }
    }

    @AfterAll
    static void tearDown() {
        if (integrationTestEnabled.equals("true")) {
            couchDbClient.deleteDatabase();
        }
    }


    @Data
    @SuperBuilder
    @AllArgsConstructor
    @NoArgsConstructor
    @EqualsAndHashCode(callSuper = true)
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class ParentCouchEntity extends Document {
        private String names;
        private String description;
        private Integer amount;
        private Double price;
        private List<String> childrenIds;

        @Override
        public String toString() {
            return JSON.toJson(this);
        }
    }

    @Data
    @SuperBuilder
    @AllArgsConstructor
    @NoArgsConstructor
    @EqualsAndHashCode(callSuper = true)
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class ChildCouchEntity extends Document {
        private String names;
        private Integer version;
        private Double weight;

        @Override
        public String toString() {
            return JSON.toJson(this);
        }
    }

    public static class ParentDAO extends AbstractCouchDAO<ParentCouchEntity> {
        public ParentDAO(CouchDBClient client) {
            super(client, ParentCouchEntity.class);
        }

    }

    public static class ChildDAO extends AbstractCouchDAO<ChildCouchEntity> {
        public ChildDAO(CouchDBClient client) {
            super(client, ChildCouchEntity.class);
        }

    }
}
