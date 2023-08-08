package org.connector.impl;

import lombok.Getter;
import org.connector.model.Document;

import java.util.List;

public record CouchFindResult<X extends Document>(List<X> results, String bookmark) {}