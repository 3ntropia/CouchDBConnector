package org.connector.dao;


import org.connector.dao.query.entities.*;
import org.connector.impl.CouchPaginator;
import org.connector.model.CouchQueryViewResponse;
import org.connector.model.CreateIndexRequest;
import org.connector.model.FindRequest;
import org.connector.model.IndexDefinition;
import org.connector.model.ViewMap;
import org.connector.model.ViewName;
import org.connector.model.ViewRequest;
import org.connector.query.CouchQuery;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.condition.EnabledIfEnvironmentVariable;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.connector.dao.ConstantTest.INMUTABLE_STRING;
import static org.connector.query.IntegrationConstants.COUCH_QUERY_PARAM_ID;
import static org.junit.jupiter.api.Assertions.*;

@EnabledIfEnvironmentVariable(named = "INTEGRATION_DB", matches = "true")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class AbstractCouchDAOTest extends AbstractCouchDbIntegrationTest {

    @Test
    void dbClientIsCreated() {
        assertNotNull(couchDbClient);
    }

    @Test
    @Order(1)
    void generateDataTest(){
        SomeClass fooClass = new SomeClass("test field", "extra", "1:1");
        SomeClass fooClass2 = new SomeClass("test field2", "Extra", "1:2", Arrays.asList(new InnerClass("1", "1"), new InnerClass("prop 2", "2")));
        SomeClass fooClass3 = new SomeClass("test field3", "exxxxtra", "1:3");
        SomeClass fooClass4 = new SomeClass("test field4", "extra1", "1:4", Arrays.asList(new InnerClass("prop 1", "1"),
                new InnerClass("prop 2", "2"), new InnerClass("prop 3", "3") , new InnerClass("prop 5", "5")));
        SomeClass fooClass5 = new SomeClass("test field5", "extra2", "1:5", Arrays.asList(new InnerClass("3", "3"), new InnerClass("4", "4")));
        SomeClass fooClass6 = new SomeClass("test field6", "extra3", "1:6");
        List<SomeClass> response = someDAO.create(Arrays.asList(fooClass, fooClass2, fooClass3, fooClass4, fooClass5, fooClass6));
        assertNotNull(response);
        assertEquals(6, response.size());
    }

    @Test
    @Order(2)
    void queryDataSet(){
        CouchQuery couchQuery = CouchQuery.and()
                .condition(COUCH_QUERY_PARAM_ID, "1:6")
                .partition("1");
        List<SomeClass> test = someDAO.find(couchQuery);
        assertNotNull(test);
        assertNotEquals(0, test.size());
        assertEquals("1:6", test.get(0).getId());
    }

    @Test
    @Order(3)
    void createIndex(){
        String INDEX = "index-name";
        var resp = someDAO.getClient().createIndex(CreateIndexRequest.builder()
                .name(INDEX)
                .type("json")
                .index(IndexDefinition.builder()
                        .fields(List.of("field"))
                        .build())
                .build(), "");
        assertNotNull(resp);
        assertEquals(INDEX, resp.name());
    }

    @Test
    @Order(4)
    void createIndexTest(){
        var resp = someDAO.getClient().createIndex(CreateIndexRequest.builder()
                .name("index-name")
                .type("json")
                .index(IndexDefinition.builder()
                        .fields(List.of("field"))
                        .build())
                .build(), "");
        assertNotNull(resp);
    }

    @Test
    @Order(5)
    void queryIndexTest(){
        var response = someDAO.getClient().indexExists("index-name");
        assertTrue(response);
    }

    @Test
    @Order(7)
    void queryAllDocs(){
        CouchQuery couchQuery = CouchQuery.and()
                .condition("inmutable", INMUTABLE_STRING)
                .partition("1");
        List<SomeClass> test = someDAO.find(couchQuery);
        assertNotNull(test);
        assertNotEquals(0, test.size());
        Assertions.assertEquals("1:6", test.get(5).getId());
    }

    @Test
    @Order(8)
    void queryCouchQueryRequest(){
        var couchQueryRequest = FindRequest.builder()
                .selector("{\"extraField\":\"extra\"}")
                .limit(2)
                //.sort("{\"document.extraField\":\"ASC\"}")
                .partition("1")
                .build();
        List<SomeClass> test = someDAO.find(couchQueryRequest, "");
        assertNotNull(test);
        assertEquals(test.get(0).getField(), "test field");

        var regexQuery = FindRequest.builder()
                .selector("{\"extraField\": {\"$regex\": \"^exx\"}}")
                .partition("1")
                .build();
        List<SomeClass> test2 = someDAO.find(regexQuery, "");
        assertNotNull(test2);
        assertEquals("test field3", test2.get(0).getField());
    }

    @Test
    @Order(9)
    void querySubClass() {
        var couchQueryRequest = FindRequest.builder()
                .selector("{\"extraField\":\"extra\"}")
                .fields(Collections.singletonList("field"))
                .partition("1")
                .build();
        List<ExtraClass> test2 = someDAO.findBySubClass(couchQueryRequest, ExtraClass.class);
        assertNotNull(test2);
        assertEquals("test field", test2.get(0).getField());
    }

    @Test
    @Order(10)
    void queryInTest() {
        String[] IDS = new String[] {"1:6", "1:4"};
        CouchQuery couchQuery = CouchQuery.and()
                .in(COUCH_QUERY_PARAM_ID, IDS)
                .partition("1");
        List<SomeClass> test = someDAO.find(couchQuery);
        assertNotNull(test);
        assertEquals(2, test.size());
    }

    @Test
    @Order(11)
    void queryPageTest() {
        CouchQuery couchQuery = CouchQuery.and()
                .condition("inmutable", INMUTABLE_STRING).partition("1");
        CouchPaginator<SomeClass> couchPaginator = new CouchPaginator<>(couchQuery, someDAO, 2);
        List<SomeClass> firstPage = couchPaginator.next();
        List<SomeClass> secondPage = couchPaginator.next();
        assertNotNull(firstPage);
        assertNotNull(secondPage);
        assertEquals("1:1", firstPage.get(0).getId());
        assertEquals("1:3", secondPage.get(0).getId());
    }

    @Test
    @Order(12)
    void querySecondPageTest() {
        CouchQuery couchQuery = CouchQuery.and()
                .condition("inmutable", INMUTABLE_STRING)
                .partition("1");
        CouchPaginator<SomeClass> couchPaginator = new CouchPaginator<>(couchQuery, someDAO, 2);
        List<SomeClass> secondPage = couchPaginator.getPage(1);
        assertNotNull(secondPage);
        assertEquals("1:3", secondPage.get(0).getId());
    }

    @Test
    @Order(14)
    void queryPages() {
        var fooClasses = CouchDbEntitiesFactory.createFooClass();
        someDAO.create(fooClasses);
        CouchQuery couchQuery = CouchQuery.and()
                .condition("inmutable", INMUTABLE_STRING)
                .partition("1");
        CouchPaginator<SomeClass> couchPaginator = new CouchPaginator<>(couchQuery, someDAO, 2);
        List<SomeClass> firstPage = couchPaginator.next();
        List<SomeClass> secondPage = couchPaginator.next();
        assertNotNull(firstPage);
        assertNotNull(secondPage);
        assertEquals("1:1", firstPage.get(0).getId());
        assertEquals("1:3", secondPage.get(0).getId());
    }

    @Test
    @Order(15)
    void queryIn() {
        final String[] IDS = new String[] {"1:6", "1:4"};
        var fooClasses = CouchDbEntitiesFactory.createFooClass();
        someDAO.create(fooClasses);
        CouchQuery couchQuery = CouchQuery.and()
                .in(COUCH_QUERY_PARAM_ID, IDS)
                .partition("1");
        List<SomeClass> test = someDAO.find(couchQuery);
        assertEquals(2, test.size());
    }

    @Test
    @Order(16)
    void queryNotIn() {
        final String[] IDS = new String[] {"1:6", "1:4"};
        var fooClasses = CouchDbEntitiesFactory.createFooClass();
        someDAO.create(fooClasses);
        CouchQuery couchQuery = CouchQuery.and()
                .notIn(COUCH_QUERY_PARAM_ID, IDS)
                .partition("1");
        List<SomeClass> test = someDAO.find(couchQuery);
        assertEquals(4, test.size());
    }

    @Test
    @Order(17)
    void querySecondPages() {
        var fooClasses = CouchDbEntitiesFactory.createFooClass();
        someDAO.create(fooClasses);
        CouchQuery couchQuery = CouchQuery.and()
                .condition("inmutable", INMUTABLE_STRING)
                .partition("1");
        CouchPaginator<SomeClass> couchPaginator = new CouchPaginator<>(couchQuery, someDAO, 2);
        List<SomeClass> secondPage = couchPaginator.getPage(1);
        assertNotNull(secondPage);
        assertEquals("1:3", secondPage.get(0).getId());
    }

    @Test
    @Order(18)
    void queryByDAOId() {
        var fooClass = someDAO.getById("1:1");
        assertNotNull(fooClass);
        assertEquals("1:1", fooClass.getId());
    }

    @Test
    @Order(19)
    void bulkGetByIds() {
        var someClasses = someDAO.getByIds(Arrays.asList("1:4", "1:2"));
        assertNotNull(someClasses);
        assertEquals(2, someClasses.size());
        assertEquals("1:4", someClasses.get(0).getId());
        assertEquals("1:2", someClasses.get(1).getId());
    }

    @Test
    @Order(20)
    void bulkCreate() {
        SomeClass someClass = SomeClass.builder().id("1:7").field("fields1").build();
        SomeClass someClass2 = SomeClass.builder().id("1:8").field("fields2").build();
        var someClasses = someDAO.create(Arrays.asList(someClass, someClass2));
        assertNotNull(someClasses);
        assertEquals(2, someClasses.size());
        assertEquals("1:7", someClasses.get(0).getId());
        assertEquals("1:8", someClasses.get(1).getId());
    }

    @Test
    @Order(21)
    void bulkDelete() {
        var someClasses = someDAO.delete(Arrays.asList("1:7", "1:8"));
        assertTrue(someClasses);
    }

    @Test
    @Order(22)
    void createSingleDocument() {
        var someClas = someDAO.create(SomeClass
                .builder()
                .id("1:9")
                .field("fields1").build());
        assertNotNull(someClas);
        assertEquals("1:9", someClas.getId());
        assertNotNull(someClas.getRev());
    }

    @Test
    @Order(22)
    void updateSingleDocument() {
        var some = someDAO.getById("1:9");
        some.setField("field modified");
        var someClas = someDAO.update(some);
        assertNotNull(someClas);
        assertEquals("1:9", someClas.getId());
        assertEquals("field modified", someClas.getField());
        assertNotNull(someClas.getRev());
    }

    @Test
    @Order(22)
    void deleteingleDocument() {
        var deleted = someDAO.delete("1:9");
        assertTrue(deleted);
    }
}