package org.connector.http;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Map;
import java.util.Set;

@Data
@AllArgsConstructor
public class CouchHttpHeaders {
    private Map<String, ?> headers;

    public Set<String> keys() {
        return headers.keySet();
    }

    public Object get(String key) {
        return headers.get(key);
    }

    public static CouchHttpHeaders of(Map<String, ?> headers) {
        return new CouchHttpHeaders(headers);
    }
}
