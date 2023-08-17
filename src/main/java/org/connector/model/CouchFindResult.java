package org.connector.model;

import java.util.List;

public record CouchFindResult<X extends Document>(List<X> results, String bookmark) {}