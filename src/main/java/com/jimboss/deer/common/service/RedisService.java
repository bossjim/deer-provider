package com.jimboss.deer.common.service;

import com.jimboss.deer.common.exception.RedisConnectException;

import java.util.Set;

public interface RedisService {
    /**
     * get命令
     *
     * @param key key
     * @return String
     */
    String get(String key) throws RedisConnectException;

    /**
     * set命令
     *
     * @param key   key
     * @param value value
     * @return String
     */
    String set(String key, String value) throws RedisConnectException;

    /**
     * set 命令
     *
     * @param key         key
     * @param value       value
     * @param milliscends 毫秒
     * @return String
     */
    String set(String key, String value, Long milliscends) throws RedisConnectException;

    /**
     * pexpire命令
     *
     * @param key         key
     * @param milliscends 毫秒
     * @return Long
     */
    Long pexpire(String key, Long milliscends) throws RedisConnectException;

    /**
     * del命令
     *
     * @param key key
     * @return Long
     */
    Long del(String... key) throws RedisConnectException;

    /**
     * zadd 命令
     *
     * @param key    key
     * @param score  score
     * @param member value
     */
    Long zadd(String key, Double score, String member) throws RedisConnectException;

    /**
     * zrangeByScore 命令
     *
     * @param key key
     * @param min min
     * @param max max
     * @return Set<String>
     */
    Set<String> zrangeByScore(String key, String min, String max) throws RedisConnectException;

    /**
     * zrem 命令
     *
     * @param key     key
     * @param members members
     * @return Long
     */
    Long zrem(String key, String... members) throws RedisConnectException;

}
