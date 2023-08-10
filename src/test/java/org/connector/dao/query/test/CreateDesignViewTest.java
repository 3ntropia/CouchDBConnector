package org.connector.dao.query.test;

import org.connector.impl.CouchDBClient;
import org.connector.model.ViewMap;
import org.connector.model.ViewName;
import org.connector.model.ViewRequest;
import org.connector.dao.query.CouchDBClientUtil;
import com.marvel.menu.AbstractIntegrationTest;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class CreateDesignViewTest extends AbstractIntegrationTest {

    @Override
    public Map<String, Object> run(Map<String, Object> testContext) throws Exception {

        CouchDBClient couchDBClient = CouchDBClientUtil.buildClient();
        testContext = new HashMap<>();
        ViewRequest viewRequest = ViewRequest.builder()
                .views(new ViewName(new ViewMap("function (doc) { if(doc.document.innerClass !== null){  doc.document.innerClass.forEach(c => { emit(c.id, doc.document.id); }); } }")))
                .language("javascript")
                .build();
        Boolean viewCreated = couchDBClient.createView(viewRequest, "test");
        assertTrue(viewCreated);
        return testContext;

    }
}
