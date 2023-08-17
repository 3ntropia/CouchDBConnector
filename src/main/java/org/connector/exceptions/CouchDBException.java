package org.connector.exceptions;

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

    public CouchDBException(int statusCode, String method, String uri, String reason) {

    }
}
