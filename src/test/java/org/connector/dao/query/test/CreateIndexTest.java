package org.connector.dao.query.test;

import org.connector.impl.CouchDBClient;
import org.connector.model.CreateIndexRequest;
import org.connector.model.IndexDefinition;
import org.connector.dao.query.CouchDBClientUtil;
import org.connector.dao.query.entities.SomeDAO;
import com.marvel.menu.AbstractIntegrationTest;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertNotNull;

public class CreateIndexTest extends AbstractIntegrationTest {

    @Override
    public Map<String, Object> run(Map<String, Object> testContext) throws Exception {
        testContext = new HashMap<>();
        CouchDBClient couchDBClient = CouchDBClientUtil.buildClient();
        SomeDAO someDAO = new SomeDAO(couchDBClient);
        var resp = someDAO.getClient().createIndex(CreateIndexRequest.builder()
                .name("index-name")
                .type("json")
                .index(IndexDefinition.builder()
                        .fields(List.of("field"))
                        .build())
                .build(), "");
        assertNotNull(resp);
        return testContext;
    }
}
