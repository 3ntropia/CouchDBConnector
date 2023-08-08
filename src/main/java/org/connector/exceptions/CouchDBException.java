package org.connector.exceptions;

import java.io.IOException;

public class CouchDBException extends RuntimeException {

    public CouchDBException(String errorMessage, Exception e) {
        super(errorMessage, e);
    }

    public CouchDBException(String errorMessage) {
        super(errorMessage);
    }

    public CouchDBException(Exception e){
        super(e);
    }
}
