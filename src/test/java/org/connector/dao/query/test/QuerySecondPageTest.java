package org.connector.dao.query.test;

import org.connector.impl.CouchDBClient;
import org.connector.impl.CouchPaginator;
import org.connector.dao.query.CouchDBClientUtil;
import org.connector.query.CouchQuery;
import org.connector.dao.query.entities.SomeClass;
import org.connector.dao.query.entities.SomeDAO;
import com.marvel.menu.AbstractIntegrationTest;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class QuerySecondPageTest extends AbstractIntegrationTest {

    @Override
    public Map<String, Object> run(Map<String, Object> testContext) {
        CouchDBClient couchDBClient = CouchDBClientUtil.buildClient();
        SomeDAO someDAO = new SomeDAO(couchDBClient);
        CouchQuery couchQuery = CouchQuery._and()
                .condition("type", "SomeClass")
                .partition("1");
        CouchPaginator<SomeClass> couchPaginator = new CouchPaginator<>(couchQuery, someDAO, 2);
        List<SomeClass> secondPage = couchPaginator.getPage(1);
        assertNotNull(secondPage);
        assertEquals("1:3", secondPage.get(0).getId());
        return testContext;
    }
}
