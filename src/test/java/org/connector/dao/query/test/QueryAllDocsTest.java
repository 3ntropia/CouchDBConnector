package org.connector.dao.query.test;

import org.connector.impl.CouchDBClient;
import org.connector.dao.query.CouchDBClientUtil;
import org.connector.query.CouchQuery;
import org.connector.dao.query.entities.SomeClass;
import org.connector.dao.query.entities.SomeDAO;
import com.marvel.menu.AbstractIntegrationTest;
import org.junit.jupiter.api.Assertions;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class QueryAllDocsTest extends AbstractIntegrationTest {

    @Override
    public Map<String, Object> run(Map<String, Object> testContext) throws Exception {
        CouchDBClient couchDBClient = CouchDBClientUtil.buildClient();
        SomeDAO someDAO = new SomeDAO(couchDBClient);
        CouchQuery couchQuery = CouchQuery._and()
                .condition("type", "SomeClass")
                .partition("1");
        List<SomeClass> test = someDAO.find(couchQuery);
        assertNotNull(test);
        assertNotEquals(0, test.size());
        Assertions.assertEquals("1:6", test.get(5).getId());
        return testContext;
    }
}
