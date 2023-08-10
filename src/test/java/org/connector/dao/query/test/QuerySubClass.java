package org.connector.dao.query.test;

import org.connector.impl.CouchDBClient;
import org.connector.model.FindRequest;
import org.connector.dao.query.CouchDBClientUtil;
import org.connector.dao.query.entities.ExtraClass;
import org.connector.dao.query.entities.SomeDAO;
import com.marvel.menu.AbstractIntegrationTest;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class QuerySubClass extends AbstractIntegrationTest {

    @Override
    public Map<String, Object> run(Map<String, Object> testContext) throws Exception {
        CouchDBClient couchDBClient = CouchDBClientUtil.buildClient();
        SomeDAO someDAO = new SomeDAO(couchDBClient);
        var couchQueryRequest = FindRequest.builder()
                .selector("{\"document.extraField\":\"extra\"}")
                .fields(Collections.singletonList("document.id"))
                .partition("1")
                .build();
        List<ExtraClass> test2 = someDAO.findBySubClass(couchQueryRequest, ExtraClass.class);
        assertNotNull(test2);
        assertEquals("test field", test2.get(0).getField());
        return testContext;
    }

}
