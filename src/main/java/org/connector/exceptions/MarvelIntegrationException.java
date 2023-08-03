package org.connector.exceptions;

import java.util.concurrent.atomic.AtomicInteger;

public class MarvelIntegrationException extends RuntimeException {
	private static final long serialVersionUID = -2200090674591338114L;
	private final AtomicInteger statusCode = new AtomicInteger();

	public MarvelIntegrationException() {
		super();
	}

	public MarvelIntegrationException(String message) {
		super(message);
	}

	public MarvelIntegrationException(Throwable cause) {
		super(cause);
	}

	public MarvelIntegrationException(String message, Throwable cause) {
		super(message, cause);
	}

	public MarvelIntegrationException(String message, int statusCode) {
		super(message);
		this.statusCode.set(statusCode);
	}

	public int getStatusCode() {
		return statusCode.get();
	}

}
