# CouchDBConnector

# Java Library for CouchDB Database Access

This Java library simplifies the interaction with CouchDB databases. It offers a range of features for performing bulk operations, including GET, DELETE, and UPDATE. Additionally, it provides methods for creating databases, indexes, views, and partitions.

## Usage

### Extending `AbstractCouchDAO`

One way to use this library is by extending the `AbstractCouchDAO` class, which implements basic CRUD operations similarly to JPA (Java Persistence API).

`public class SomeDAO extends AbstractCouchDAO<SomeClass>`

### Using `CouchDBClient`

Alternatively, you can use the `CouchDBClient` connector, which can be instantiated using a builder as shown below:

```java
couchDbClient = CouchDBClient.builder()
                    .url(Protocol.HTTP.getValue(), dbHost, dbPort)
                    .database(dbName)
                    .username(dbUser)
                    .password(dbPass)
                    .maxConnections(10)
                    .createDatabase(true)
                    .build();

