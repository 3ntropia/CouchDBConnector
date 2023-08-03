package org.connector.api;

import org.connector.model.CouchQueryViewResponse;
import org.connector.http.CouchHttpHeaders;
import org.jetbrains.annotations.Nullable;

import java.util.Map;

/**
 * Api Reference https://docs.couchdb.org/en/latest/api/ddoc/index.html
 */
interface DesignDocsInterface {

    CouchHttpHeaders getDesignDocHeads(String ddoc);

    CouchQueryViewResponse getDesignDocView(String ddoc, String view, @Nullable String partition,
                                            @Nullable Map<String,Object> queryParams);

    CouchQueryViewResponse findView(String partition, String designDocName, String viewName);

    CouchQueryViewResponse findView(String partition, String designDocName, String viewName, String key);

    CouchQueryViewResponse findView(String partition, String designDocName, String viewName, String starKey, String endKey);

}
