package com.zsxq.demo.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * 知识星球配置属性
 */
@Data
@Configuration
@ConfigurationProperties(prefix = "zsxq")
public class ZsxqProperties {

    /**
     * 知识星球 Token
     */
    private String token;

    /**
     * 默认星球 ID
     */
    private Long groupId;

    /**
     * 请求超时时间（毫秒）
     */
    private Integer timeout = 10000;

    /**
     * 重试次数
     */
    private Integer retryCount = 3;

    /**
     * 重试延迟（毫秒）
     */
    private Integer retryDelay = 1000;
}
