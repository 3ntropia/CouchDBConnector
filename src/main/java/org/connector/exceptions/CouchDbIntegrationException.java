package org.connector.exceptions;

import org.jetbrains.annotations.NotNull;

public class CouchDbIntegrationException extends MarvelIntegrationException {

    public CouchDbIntegrationException(int statusCode, @NotNull String method, @NotNull String uri, @NotNull String reason) {
        super(String.format("%s when trying %s on %s with reason: %s", statusCode, method, uri, reason), statusCode);
    }

}
