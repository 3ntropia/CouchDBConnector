package org.connector.dao;

import org.apache.commons.codec.binary.Base64;
import org.connector.dao.query.entities.Foo;
import org.connector.dao.query.entities.LightSomeClass;
import org.connector.dao.query.entities.SomeClass;
import org.connector.model.*;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.condition.EnabledIfEnvironmentVariable;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@EnabledIfEnvironmentVariable(named = "INTEGRATION_DB", matches = "true")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class CouchDBClientTest extends AbstractCouchDbIntegrationTest{

    private final Map<String, Attachment> attachmentMap = new HashMap<>();

    @Test
    @Order(1)
    void createDesignViewTest(){
        ViewRequest viewRequest = ViewRequest.builder()
                .views(new ViewName(new ViewMap("function (doc) { if(doc.innerClass !== null){  doc.innerClass.forEach(c => { emit(c._id, doc._id); }); } }")))
                .language("javascript")
                .build();
        Boolean viewCreated = couchDbClient.createView(viewRequest, "test");
        assertTrue(viewCreated);
    }

    @Test
    @Order(2)
    void queryViewsTest() {
        CouchQueryViewResponse test = couchDbClient.findView("1", "test", "viewName");
        assertNotNull(test);
        assertEquals("8", test.totalRows());
        assertEquals(8, test.getDeserializedRow().size());
    }


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
        assertNotNull(fooClassRev);
        assertEquals("1:1", fooClassRev.getId());
    }

    @Test
    void updateSpecificRevision() {
        var fooClass = couchDbClient.getDocumentById("1:1", Foo.class);
        var fooClassRev = couchDbClient.getDocumentByRev("1:1", fooClass.getRev(), Foo.class);
        fooClassRev.setType("added type");
        var fooUpdated = couchDbClient.saveDocument("1:1", fooClassRev.getRev(), fooClassRev);
        assertNotNull(fooUpdated);
        assertEquals("1:1", fooUpdated.id());
    }

    @Test
    void updateAnyRevision() {
        var fooClass = couchDbClient.getDocumentById("1:1", Foo.class);
        fooClass.setType("added type 3");
        var fooUpdated = couchDbClient.saveDocument("1:1", fooClass);
        assertNotNull(fooUpdated);
        assertEquals("1:1", fooUpdated.id());

    }

    @Test
    void queryByCouchClienteIdRevivions() {
        var fooClass = couchDbClient.getDocumentById("1:1", SomeClass.class);
        fooClass.setField("modified");
        couchDbClient.saveDocument(fooClass);
        var fooClassRevisions = couchDbClient.getDocumentById("1:1", true, Foo.class);
        assertNotNull(fooClass);
        assertEquals("1:1", fooClassRevisions.getId());
    }

    @Test
    void fetchLightDocumentDownCasting() {
        var lightSomeClass = couchDbClient.getDocumentById("1:4", LightSomeClass.class);
        assertNotNull(lightSomeClass);
        assertEquals("1:4", lightSomeClass.getId());
        assertEquals("test field4", lightSomeClass.getField());
    }

    @Test
    public void attachmentInline() {
        Attachment attachment1 = Attachment.withDataContent("VGhpcyBpcyBhIGJhc2U2NCBlbmNvZGVkIHRleHQ=", "text/plain");

        Attachment attachment2 = Attachment.withDataContent(Base64.encodeBase64String("binary string".getBytes()), "text/plain");

        Foo bar = new Foo("2:1"); // Bar extends Document

        attachmentMap.put("txt_1.txt", attachment1);
        attachmentMap.put("txt_2.txt", attachment2);
        bar.setAttachments(attachmentMap);

        couchDbClient.saveDocument(bar);
    }


    @Test
    public void attachmentInline_getWithDocument() {
        Attachment attachment = Attachment.withDataContent("VGhpcyBpcyBhIGJhc2U2NCBlbmNvZGVkIHRleHQ=", "text/plain");
        Map<String, Attachment> attachmentMap = new HashMap<>();
        attachmentMap.put("txt_1.txt", attachment);
        Foo foo = new Foo("2:5");
        foo.setAttachments(attachmentMap);

        SaveResponse response = couchDbClient.saveDocument(foo);

        Foo foo2 = couchDbClient.getDocumentById(response.id(), Foo.class);
        String base64Data = foo2.getAttachments().get("txt_1.txt").getData();
        assertNotNull(base64Data);
    }

    @Test
    public void standaloneAttachment_docIdContainSpecialChar() {
        byte[] bytesToDB = "binary data".getBytes();
        ByteArrayInputStream bytesIn = new ByteArrayInputStream(bytesToDB);

        var foo = new Foo("2:4");

        SaveResponse respSave = couchDbClient.saveDocument(foo);

        couchDbClient.saveAttachment(bytesIn, "foo.txt", "text/plain", respSave.id(), respSave.rev());
    }

    @Test
    public void getAttachment() throws IOException {

        byte[] bytesToDB = "binary data".getBytes();

        InputStream in = couchDbClient.getAttachment( "foo.txt", "2:4");
        ByteArrayOutputStream bytesOut = new ByteArrayOutputStream();
        int n;
        while ((n = in.read()) != -1) {
            bytesOut.write(n);
        }
        bytesOut.flush();
        in.close();

        byte[] bytesFromDB = bytesOut.toByteArray();

        assertArrayEquals(bytesToDB, bytesFromDB);
    }

}
