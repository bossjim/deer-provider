package com.jimboss.deer.common.function;

@FunctionalInterface
public interface CacheSelector<T> {
    T select() throws Exception;
}
