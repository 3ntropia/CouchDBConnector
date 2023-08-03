package org.connector.http;

import org.apache.commons.lang3.StringUtils;

public enum Protocol {
    HTTP("http"),
    HTTPS("https");

    private final String value;

    public String getValue() {
        return value;
    }

    Protocol(String value) {
        this.value = value;
    }

    public static Protocol fromValue(String value) {
        if (StringUtils.equalsIgnoreCase(HTTPS.getValue(), value))
            return HTTPS;
        return HTTP;
    }
}
