package com.jimboss.deer.common.function;


import com.jimboss.deer.common.exception.RedisConnectException;

@FunctionalInterface
public interface JedisExecutor<T, R> {
    R excute(T t) throws RedisConnectException;
}
