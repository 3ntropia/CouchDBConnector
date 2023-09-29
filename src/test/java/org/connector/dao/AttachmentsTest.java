package org.connector.dao;

import org.apache.commons.codec.binary.Base64;
import org.connector.dao.query.entities.Foo;
import org.connector.model.Attachment;
import org.connector.model.SaveResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.EnabledIfEnvironmentVariable;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@EnabledIfEnvironmentVariable(named = "INTEGRATION_DB", matches = "true")
public class AttachmentsTest extends AbstractCouchDbIntegrationTest{

	private final Map<String, Attachment> attachmentMap = new HashMap<>();

	@Test
	public void attachmentInline() {
		Attachment attachment1 = Attachment.withDataContent("VGhpcyBpcyBhIGJhc2U2NCBlbmNvZGVkIHRleHQ=", "text/plain");

		Attachment attachment2 = Attachment.withDataContent(Base64.encodeBase64String("binary string".getBytes()), "text/plain");

		Foo bar = new Foo("1:" + generateUUID()); // Bar extends Document

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
		Foo foo = new Foo("1:1234");
		foo.setAttachments(attachmentMap);

		SaveResponse response = couchDbClient.saveDocument(foo);

		Foo foo2 = couchDbClient.getDocumentById(response.id(), Foo.class);
		String base64Data = foo2.getAttachments().get("txt_1.txt").getData();
		assertNotNull(base64Data);
	}
	
	@Test
	public void standaloneAttachment_newDocumentGivenId() throws IOException {
		byte[] bytesToDB = "binary data".getBytes();
		ByteArrayInputStream bytesIn = new ByteArrayInputStream(bytesToDB);
		
		String docId = generateUUID();
		
		couchDbClient.saveAttachment(bytesIn, "foo.txt", "text/plain", "1:"+ docId, null);
	}
	
	@Test
	public void standaloneAttachment_existingDocument() {
		byte[] bytesToDB = "binary data".getBytes();
		ByteArrayInputStream bytesIn = new ByteArrayInputStream(bytesToDB);

		SaveResponse respSave = couchDbClient.saveDocument(new Foo("1:" + generateUUID()));
		
		couchDbClient.saveAttachment(bytesIn, "foo.txt", "text/plain", respSave.id(), respSave.rev());
	}
	
	@Test
	public void standaloneAttachment_docIdContainSpecialChar() {
		byte[] bytesToDB = "binary data".getBytes();
		ByteArrayInputStream bytesIn = new ByteArrayInputStream(bytesToDB);

		SaveResponse respSave = couchDbClient.saveDocument(new Foo("i:" + generateUUID()));
		
		couchDbClient.saveAttachment(bytesIn, "foo.txt", "text/plain", respSave.id(), respSave.rev());
	}
	
	// Helper
	
	private static String generateUUID() {
		return UUID.randomUUID().toString().replace("-", "");
	}
}
