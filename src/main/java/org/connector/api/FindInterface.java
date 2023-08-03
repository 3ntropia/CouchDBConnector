package org.connector.api;

import org.connector.model.FindRequest;
import org.connector.model.FindResponse;
import org.connector.model.Document;

interface FindInterface {
    <T extends Document> FindResponse<T> find(FindRequest request, String partition, Class<T> clazz);

}
