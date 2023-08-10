package org.connector.dao;


import org.connector.impl.CouchPaginator;
import org.connector.model.GetIndexResponse;
import org.connector.query.CouchQuery;
import org.connector.dao.query.entities.SomeClass;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.EnabledIfEnvironmentVariable;
import org.junit.jupiter.api.condition.EnabledIfEnvironmentVariables;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@EnabledIfEnvironmentVariable(named = "INTEGRATION_DB", matches = "true")
class AbstractCouchDAOTest extends AbstractCouchDbIntegrationTest {

    @Test
    @EnabledIfEnvironmentVariables({
            @EnabledIfEnvironmentVariable(named = "INTEGRATION_DB", matches = "true"),
            @EnabledIfEnvironmentVariable(named = "TEST_CONTAINER_DB", matches = "true")
    })
    void couchDbContainerStartsTest() {
        assertTrue(COUCH_DB.isRunning());
    }

    @Test
    void dbClientIsCreated() {
        assertNotNull(couchDbClient);
    }

    @Test
    void createEntityWithGenId() {
        var child1 = CouchDbEntitiesFactory.createChild("ChildEntity1");
        var created = childDAO.create(child1);
        var splitted = created.getId().split(":");
        //assertEquals(marvelContext.getTenant().getName(), splitted[0]);
        //assertEquals(marvelContext.getTenant().getName(), created.getTenantId());
        //assertEquals(marvelContext.getSandbox(), created.getSandbox());
    }

    //@Test
    //void createAndListTest() {
    //    var parent = CouchDbEntitiesFactory.createParent("CreateAndListParent", Collections.emptyList());
    //    var parentId = parentDAO.create(parent).getId();
    //    var parentListResult = parentDAO.list(marvelContext);
//
    //    assertTrue(1 <= parentListResult.size());
    //    assertTrue(toIds.apply(parentListResult).contains(parentId));
    //}

    //@Test
    //void createParentWithDeletedChildTest() {
    //    // create child, get id
    //    var child1 = CouchDbEntitiesFactory.createChild("menuItem1");
    //    var child1Id = childDAO.create(child1).getId();
    //    var child2 = CouchDbEntitiesFactory.createChild("menuItem2");
    //    var child2Id = childDAO.create(child2).getId();
    //    var childIds = List.of(child1Id, child2Id);
    //    //create parent with child id.
    //    var parent = CouchDbEntitiesFactory.createParent("menu", childIds);
    //    var parentId = parentDAO.create(parent).getId();
//
    //    var parentGetByIdResult = parentDAO.getByIdAndContext(parentId);
    //    assertNotNull(parentGetByIdResult);
    //    assertEquals(2, parentGetByIdResult.getChildrenIds().size());
    //    assertTrue(parentGetByIdResult.getChildrenIds().containsAll(childIds));
//
    //    //delete child1
    //    childDAO.delete(child1Id);
    //    assertEquals(2, parentDAO.getByIdAndContext(parentId).getChildrenIds().size());
    //    assertFalse(childDAO.getNotDeletedIds(childIds).contains(child1Id));
//
    //    System.out.println("parent lookup by name 'me': " + parentDAO.listContainingName("me"));
    //    System.out.println("parent lookup by name 'menu': " + parentDAO.listContainingName("menu"));
    //}


    @Test
    void queryPages() {
        var fooClasses = CouchDbEntitiesFactory.createFooClass();
        someDAO.create(fooClasses);
        CouchQuery couchQuery = CouchQuery._and()
                .condition("type", "SomeClass")
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
    void queryIn() {
        final String[] IDS = new String[] {"1:6", "1:4"};
        var fooClasses = CouchDbEntitiesFactory.createFooClass();
        someDAO.create(fooClasses);
        CouchQuery couchQuery = CouchQuery._and()
                .condition("type", "SomeClass")
                .in("document.id", IDS)
                .partition("1");
        List<SomeClass> test = someDAO.find(couchQuery);;
        assertEquals(2, test.size());
    }

    @Test
    void queryNotIn() {
        final String[] IDS = new String[] {"1:6", "1:4"};
        var fooClasses = CouchDbEntitiesFactory.createFooClass();
        someDAO.create(fooClasses);
        CouchQuery couchQuery = CouchQuery._and()
                .condition("type", "SomeClass")
                .notIn("document.id", IDS)
                .partition("1");
        List<SomeClass> test = someDAO.find(couchQuery);;
        assertEquals(4, test.size());
    }

    @Test
    void querySecondPages() {
        var fooClasses = CouchDbEntitiesFactory.createFooClass();
        someDAO.create(fooClasses);
        CouchQuery couchQuery = CouchQuery._and()
                .condition("type", "SomeClass")
                .partition("1");
        CouchPaginator<SomeClass> couchPaginator = new CouchPaginator<>(couchQuery, someDAO, 2);
        List<SomeClass> secondPage = couchPaginator.getPage(1);
        assertNotNull(secondPage);
        assertEquals("1:3", secondPage.get(0).getId());
    }

    @Test
    void indexesFromFilesCreatedTest() {
        var actual = someDAO.getClient().getIndexes("").getIndexes();
        var expectedNames = List.of("no1", "no2");
        assertTrue(actual.size() >= 1);
        actual.stream()
                .filter(indexResponse -> indexResponse.getType().equals("json"))
                .map(GetIndexResponse.IndexResponse::getName)
                .forEach(name -> assertTrue(expectedNames.contains(name)));
    }
}