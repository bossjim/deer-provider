package com.jimboss.deer.common.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @ClassName DeerProperties
 * @Description TODO
 * @Author jinyong
 * @DATE 2019/7/23 10:21
 * @Version 1.0
 **/
@Data
@Configuration
@ConfigurationProperties(prefix = "deer")
public class DeerProperties {

    private ShiroProperties shiro = new ShiroProperties();

    private boolean openAopLog = true;
}
