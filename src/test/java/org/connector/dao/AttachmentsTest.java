package org.connector.dao;

import org.apache.commons.codec.binary.Base64;
import org.connector.dao.query.entities.Bar;
import org.connector.dao.query.entities.Foo;
import org.connector.model.Attachment;
import org.connector.model.SaveResponse;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;


public class AttachmentsTest extends AbstractCouchDbIntegrationTest{

	private final Map<String, Attachment> attachmentMap = new HashMap<>();

	@Test
	public void attachmentInline() {
		Attachment attachment1 = Attachment.withDataContent("VGhpcyBpcyBhIGJhc2U2NCBlbmNvZGVkIHRleHQ=", "text/plain");

		Attachment attachment2 = Attachment.withDataContent(Base64.encodeBase64String("binary string".getBytes()), "text/plain");

		Bar bar = new Bar(); // Bar extends Document

		attachmentMap.put("txt_1.txt", attachment1);
		attachmentMap.put("txt_2.txt", attachment2);
		bar.setAttachments(attachmentMap);

		couchDbClient.saveDocument(bar);
	}

	/*
	@Test
	public void attachmentInline_getWithDocument() {
		Attachment attachment = Attachment.withDataContent("VGhpcyBpcyBhIGJhc2U2NCBlbmNvZGVkIHRleHQ=", "text/plain");
		Map<String, Attachment> attachmentMap = new HashMap<>();
		attachmentMap.put("txt_1.txt", attachment);
		Bar bar = new Bar();
		bar.setAttachments(attachmentMap);

		SaveResponse response = couchDbClient.saveDocument(bar);
		
		Bar bar2 = couchDbClient.find(Bar.class, response.id(), new Params().attachments());
		String base64Data = bar2.getAttachments().get("txt_1.txt").getData();
		assertNotNull(base64Data);
	}
	
	@Test
	public void attachmentStandalone() throws IOException {
		byte[] bytesToDB = "binary data".getBytes();
		ByteArrayInputStream bytesIn = new ByteArrayInputStream(bytesToDB);
		SaveResponse response = couchDbClient.saveAttachment(bytesIn, "foo.txt", "text/plain");

		InputStream in = couchDbClient.find(response.id() + "/foo.txt");
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
	*/
	
	@Test
	public void standaloneAttachment_newDocumentGivenId() throws IOException {
		byte[] bytesToDB = "binary data".getBytes();
		ByteArrayInputStream bytesIn = new ByteArrayInputStream(bytesToDB);
		
		String docId = generateUUID();
		
		couchDbClient.saveAttachment(bytesIn, "foo.txt", "text/plain", docId, null);
	}
	
	@Test
	public void standaloneAttachment_existingDocument() throws IOException {
		byte[] bytesToDB = "binary data".getBytes();
		ByteArrayInputStream bytesIn = new ByteArrayInputStream(bytesToDB);

		SaveResponse respSave = couchDbClient.saveDocument(new Foo());
		
		couchDbClient.saveAttachment(bytesIn, "foo.txt", "text/plain", respSave.id(), respSave.rev());
	}
	
	@Test
	public void standaloneAttachment_docIdContainSpecialChar() throws IOException {
		byte[] bytesToDB = "binary data".getBytes();
		ByteArrayInputStream bytesIn = new ByteArrayInputStream(bytesToDB);

		SaveResponse respSave = couchDbClient.saveDocument(new Bar("i/" + generateUUID()));
		
		couchDbClient.saveAttachment(bytesIn, "foo.txt", "text/plain", respSave.id(), respSave.rev());
	}
	
	// Helper
	
	private static String generateUUID() {
		return UUID.randomUUID().toString().replace("-", "");
	}
}
