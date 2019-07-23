package com.jimboss.deer.common.utils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.jimboss.deer.common.properties.DeerProperties;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.util.Date;

/**
 * @ClassName JWTUtil
 * @Description TODO
 * @Author jinyong
 * @DATE 2019/7/23 10:19
 * @Version 1.0
 **/
@Slf4j
public class JWTUtil {

    private static final long EXPIRE_TIME = SpringContextUtil.getBean(DeerProperties.class).getShiro().getJwtTimeOut() * 1000;

    /**
     * 生成 token
     *
     * @param username 用户名
     * @param secret   用户的密码
     * @return token
     */
    public static String sign(String username, String secret) {
        try {
            username = StringUtils.lowerCase(username);
            Date date = new Date(System.currentTimeMillis() + EXPIRE_TIME);
            Algorithm algorithm = Algorithm.HMAC256(secret);
            return JWT.create()
                    .withClaim("username", username)
                    .withExpiresAt(date)
                    .sign(algorithm);
        } catch (Exception e) {
            log.error("error：{}", e);
            return null;
        }
    }
}
