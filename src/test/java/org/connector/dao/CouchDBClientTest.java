package org.connector.dao;

import org.connector.dao.query.entities.Foo;
import org.connector.dao.query.entities.LightSomeClass;
import org.connector.dao.query.entities.SomeClass;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.EnabledIfEnvironmentVariable;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@EnabledIfEnvironmentVariable(named = "INTEGRATION_DB", matches = "true")
public class CouchDBClientTest extends AbstractCouchDbIntegrationTest{

    @Test
    void queryByCouchClienteId() {
        var fooClass = couchDbClient.getDocumentById("1:1", Foo.class);
        assertNotNull(fooClass);
        assertEquals("1:1", fooClass.getId());
    }

    @Test
    void queryByCouchClienteIdRev() {
        var fooClass = couchDbClient.getDocumentById("1:1", Foo.class);
        var fooClassRev = couchDbClient.getDocumentByRev("1:1", fooClass.getRev(), Foo.class);
        assertNotNull(fooClass);
        assertEquals("1:1", fooClassRev.getId());
    }

    @Test
    void queryByCouchClienteIdRevivions() {
        var fooClass = couchDbClient.getDocumentById("1:1", SomeClass.class);
        fooClass.setField("modified");
        couchDbClient.saveDocument(fooClass);
        var fooClassRevitions = couchDbClient.getDocumentById("1:1", true, Foo.class);
        assertNotNull(fooClass);
        assertEquals("1:1", fooClassRevitions.getId());
    }

    @Test
    void fetchLightDocumentDownCasting() {
        var lightSomeClass = couchDbClient.getDocumentById("1:4", LightSomeClass.class);
        assertNotNull(lightSomeClass);
        assertEquals("1:4", lightSomeClass.getId());
        assertEquals("test field4", lightSomeClass.getField());
    }



}
