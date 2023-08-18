package org.connector.util;


import org.connector.exceptions.CouchDBException;

import java.util.function.Function;

@FunctionalInterface
public interface ConnectorFunction<T, R, E extends Exception> {

    R apply(T t) throws E;


    static <T, R, E extends Exception> Function<T, R> wrapEx(ConnectorFunction<T, R, E> fun) {
        return t -> {
            try {
                return fun.apply(t);
            } catch (Exception e) {
                throw new CouchDBException(e);
            }
        };

    }

}
