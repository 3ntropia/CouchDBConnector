package org.connector.api;

import org.connector.model.BulkGetRequest;
import org.connector.model.BulkGetResponse;
import org.connector.model.BulkSaveRequest;
import org.connector.model.BulkSaveResponse;
import org.connector.model.Document;
import org.jetbrains.annotations.NotNull;

interface BulkInterface {

    <T extends Document> BulkGetResponse<T> bulkGet(BulkGetRequest request, Class<T> clazz);

    <T extends Document> BulkSaveResponse bulkSave(@NotNull BulkSaveRequest<T> request);

    <T extends Document> BulkSaveResponse bulkDelete(@NotNull BulkSaveRequest<T> request);

}
