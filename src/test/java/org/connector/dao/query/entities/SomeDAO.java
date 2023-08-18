package org.connector.dao.query.entities;

import org.connector.impl.AbstractCouchDAO;
import org.connector.impl.CouchDBClient;
import org.connector.model.FindRequest;

import java.util.ArrayList;
import java.util.List;

public class SomeDAO extends AbstractCouchDAO<SomeClass> {

    public SomeDAO(CouchDBClient client) {
        super(client, SomeClass.class);
    }

    public List<SomeClass> find(FindRequest request, String partition) {
        return new ArrayList<>(this.client.find(request, partition, this.entityClass).docs());
    }

}