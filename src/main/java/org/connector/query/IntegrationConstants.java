package org.connector.query;

public abstract class IntegrationConstants {
    public static final String COUCH_QUERY_PARAM_ID = "_id";
    public static final String COUCH_QUERY_PARAM_REV = "_rev";
    public static final String COUCH_QUERY_PARAM_TYPE = "type";
    public static final String COUCH_QUERY_PARAM_DELETED = "_deleted";
    public static final String COUCH_QUERY_PARAM_DOCUMENT_TENANT_ID = "document.tenantId";
    public static final String COUCH_QUERY_PARAM_DOCUMENT_SANDBOX = "document.sandbox";
    public static final String COUCH_QUERY_PARAM_DOCUMENT_ID = "document.id";
    public static final String COUCH_QUERY_PARAM_DOCUMENT_NAMES = "document.names";
    public static final String COUCH_QUERY_PARAM_DOCUMENT_DELETED = "document.deleted";

    private IntegrationConstants(){}
}
