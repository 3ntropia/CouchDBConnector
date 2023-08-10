package org.connector.dao.query.test;

import org.connector.impl.CouchDBClient;
import org.connector.dao.query.CouchDBClientUtil;
import org.connector.dao.query.entities.SomeDAO;
import com.marvel.menu.AbstractIntegrationTest;

import java.util.Map;

public class DeleteDataBase extends AbstractIntegrationTest {

    @Override
    public Map<String, Object> run(Map<String, Object> testContext) throws Exception {
        CouchDBClient couchDBClient = CouchDBClientUtil.buildClient();
        SomeDAO someDAO = new SomeDAO(couchDBClient);
        someDAO.getClient().deleteDatabase();
        return testContext;
    }
}
