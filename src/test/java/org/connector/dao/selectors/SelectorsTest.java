package org.connector.dao.selectors;

import org.connector.selectors.Selectors;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class SelectorsTest {

    @Test
    void elemMatchTest() {
        var expectedJson = "{\"$and\":[{\"_id\":{\"$gt\":null}},{\"genre\":{\"$elemMatch\":{\"$eq\":\"Horror\"}}}]}";
        var actual = Selectors.and(
                Selectors.field("_id").greaterThan(null),
                Selectors.field("genre").elemMatch(Selectors.equalsTo("Horror"))
        ).asJson();

        assertEquals(expectedJson, actual);
    }

}