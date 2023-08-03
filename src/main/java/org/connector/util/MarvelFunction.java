package org.connector.util;


import org.connector.exceptions.MarvelIntegrationException;

import java.util.function.Function;

@FunctionalInterface
public interface MarvelFunction<T, R, E extends Exception> {

    R apply(T t) throws E;


    static <T, R, E extends Exception> Function<T, R> wrapEx(MarvelFunction<T, R, E> fun) {
        return t -> {
            try {
                return fun.apply(t);
            } catch (Exception e) {
                throw new MarvelIntegrationException(e);
            }
        };

    }

}
