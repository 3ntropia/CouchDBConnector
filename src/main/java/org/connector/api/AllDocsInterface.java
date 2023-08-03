package org.connector.api;

import org.connector.model.GetAllDocsResponse;
import org.springframework.lang.Nullable;

import java.util.Collection;

interface AllDocsInterface {

    GetAllDocsResponse getAllDocs(@Nullable String partition);

    GetAllDocsResponse postAllDocs(Collection<String> keys);
}
