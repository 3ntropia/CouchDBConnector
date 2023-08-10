package org.connector.dao.query.test;

import org.connector.impl.CouchDBClient;
import org.connector.dao.query.CouchDBClientUtil;
import org.connector.dao.query.entities.InnerClass;
import org.connector.dao.query.entities.SomeClass;
import org.connector.dao.query.entities.SomeDAO;
import com.marvel.menu.AbstractIntegrationTest;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class GenerateDataTest extends AbstractIntegrationTest {

    @Override
    public Map<String, Object> run(Map<String, Object> testContext) throws Exception {
        SomeClass fooClass = new SomeClass("test field", "extra", "1:1");
        SomeClass fooClass2 = new SomeClass("test field2", "Extra", "1:2", Arrays.asList(new InnerClass("1", "1"), new InnerClass("prop 2", "2")));
        SomeClass fooClass3 = new SomeClass("test field3", "exxxxtra", "1:3");
        SomeClass fooClass4 = new SomeClass("test field4", "extra1", "1:4", Arrays.asList(new InnerClass("prop 1", "1"),
                new InnerClass("prop 2", "2"), new InnerClass("prop 3", "3") , new InnerClass("prop 5", "5")));
        SomeClass fooClass5 = new SomeClass("test field5", "extra2", "1:5", Arrays.asList(new InnerClass("3", "3"), new InnerClass("4", "4")));
        SomeClass fooClass6 = new SomeClass("test field6", "extra3", "1:6");
        CouchDBClient couchDBClient = CouchDBClientUtil.buildClient();
        SomeDAO someDAO = new SomeDAO(couchDBClient);
        List<SomeClass> response = someDAO.create(Arrays.asList(fooClass, fooClass2, fooClass3, fooClass4, fooClass5, fooClass6));
        return testContext;
    }

}
