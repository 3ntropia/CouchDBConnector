package org.connector.dao.query.entities;

import org.connector.impl.AbstractCouchDAO;
import org.connector.impl.CouchDBClient;
import org.connector.model.FindRequest;

import java.util.List;
import java.util.stream.Collectors;

public class SomeDAO extends AbstractCouchDAO<SomeClass> {

    public SomeDAO(CouchDBClient client) {
        super(client, SomeClass.class);
    }

    public List<SomeClass> find(FindRequest request, String partition) {
        return this.client.find(request, partition, this.entityClass).getDocs().stream()
                .map(DocumentWrapper::getDocument)
                .collect(Collectors.toList());
    }

}