package org.connector.dao.query.test;

import org.connector.impl.CouchDBClient;
import org.connector.model.FindRequest;
import org.connector.dao.query.CouchDBClientUtil;
import org.connector.dao.query.entities.SomeClass;
import org.connector.dao.query.entities.SomeDAO;
import com.marvel.menu.AbstractIntegrationTest;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class QueryCouchQueryRequest extends AbstractIntegrationTest {

    @Override
    public Map<String, Object> run(Map<String, Object> testContext) throws Exception {
        CouchDBClient couchDBClient = CouchDBClientUtil.buildClient();
        SomeDAO someDAO = new SomeDAO(couchDBClient);
        var couchQueryRequest = FindRequest.builder()
                .selector("{\"document.extraField\":\"extra\"}")
                .limit(2)
                //.sort("{\"document.extraField\":\"ASC\"}")
                .partition("1")
                .build();
        List<SomeClass> test = someDAO.find(couchQueryRequest, "");
        assertNotNull(test);
        assertEquals(test.get(0).getField(), "test field");

        var regexQuery = FindRequest.builder()
                .selector("{\"document.extraField\": {\"$regex\": \"^exx\"}}")
                .partition("1")
                .build();
        List<SomeClass> test2 = someDAO.find(regexQuery, "");
        assertNotNull(test2);
        assertEquals("test field3", test2.get(0).getField());

        return testContext;
    }
}
