package org.connector.dao.query.test;

import org.connector.impl.CouchDBClient;
import org.connector.dao.query.CouchDBClientUtil;
import org.connector.query.CouchQuery;
import org.connector.dao.query.entities.SomeClass;
import org.connector.dao.query.entities.SomeDAO;
import com.marvel.menu.AbstractIntegrationTest;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class QueryDataSet extends AbstractIntegrationTest {

    private static final String ID = "1:6";

    @Override
    public Map<String, Object> run(Map<String, Object> testContext) throws Exception {
        CouchDBClient couchDBClient = CouchDBClientUtil.buildClient();
        SomeDAO someDAO = new SomeDAO(couchDBClient);
        CouchQuery couchQuery = CouchQuery._and()
                .condition("document.id", ID)
                .partition("1");
        List<SomeClass> test = someDAO.find(couchQuery);
        assertNotNull(test);
        assertNotEquals(0, test.size());
        assertEquals(ID, test.get(0).getId());
        return testContext;
    }



}
