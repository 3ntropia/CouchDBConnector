package org.connector.dao.query;

import org.connector.impl.AbstractCouchDAO;
import org.connector.impl.CouchDBClient;

public class DaoMock extends AbstractCouchDAO<PersonEntityMock> {

    public DaoMock(CouchDBClient client) {
        super(client, PersonEntityMock.class);
    }

}
