package org.connector.dao.query.test;

import org.connector.impl.CouchDBClient;
import org.connector.model.CouchQueryViewResponse;
import org.connector.dao.query.CouchDBClientUtil;
import org.connector.dao.query.entities.SomeDAO;
import com.marvel.menu.AbstractIntegrationTest;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class QueryViewsTest extends AbstractIntegrationTest {

    @Override
    public Map<String, Object> run(Map<String, Object> testContext) throws Exception {
        CouchDBClient couchDBClient = CouchDBClientUtil.buildClient();
        SomeDAO someDAO = new SomeDAO(couchDBClient);
        CouchQueryViewResponse test = someDAO.getClient().findView("1", "test", "viewName");
        assertNotNull(test);
        assertEquals("8", test.getTotalRows());
        assertEquals(8, test.getDeserializedRow().size());
        return testContext;
    }

}
