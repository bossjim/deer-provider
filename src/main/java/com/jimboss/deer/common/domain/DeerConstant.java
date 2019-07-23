package com.jimboss.deer.common.domain;

/**
 * @ClassName DeerConstant
 * @Description DEER常量
 * @Author jinyong
 * @DATE 2019/7/22 17:12
 * @Version 1.0
 **/
public class DeerConstant {
    // user缓存前缀
    public static final String USER_CACHE_PREFIX = "deer.cache.user.";

    // token缓存前缀
    public static final String TOKEN_CACHE_PREFIX = "deer.cache.token.";

    // 存储在线用户的 zset前缀
    public static final String ACTIVE_USERS_ZSET_PREFIX = "deer.user.active";

    // user角色缓存前缀
    public static final String USER_ROLE_CACHE_PREFIX = "deer.cache.user.role.";

    // user权限缓存前缀
    public static final String USER_PERMISSION_CACHE_PREFIX = "deer.cache.user.permission.";

    // user个性化配置前缀
    public static final String USER_CONFIG_CACHE_PREFIX = "deer.cache.user.config.";

}
