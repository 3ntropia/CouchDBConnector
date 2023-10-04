package org.connector.api;

import org.connector.model.*;
import org.springframework.lang.NonNull;

import java.util.List;

public interface IBulk {

    <T extends Document> BulkGetResponse<T> bulkGet(@NonNull BulkGetRequest request, @NonNull Class<T> clazz);

    <T extends Document> BulkSaveResponse bulkSave(@NonNull BulkSaveRequest<T> request);

    <T extends Document> BulkSaveResponse bulkDelete(@NonNull BulkSaveRequest<T> request);

    <T extends Document> List<T> bulkGetByIds(@NonNull List<String> ids,@NonNull Class<T> clazz);

    <T extends Document> List<T> bulkGetByIds(@NonNull Class<T> clazz, String... ids);
}
