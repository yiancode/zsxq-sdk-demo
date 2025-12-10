package com.zsxq.demo.config;

import com.zsxq.sdk.client.ZsxqClient;
import com.zsxq.sdk.client.ZsxqClientBuilder;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 知识星球 SDK 配置
 */
@Configuration
@RequiredArgsConstructor
public class ZsxqConfig {

    private final ZsxqProperties properties;

    @Bean
    public ZsxqClient zsxqClient() {
        return new ZsxqClientBuilder()
                .token(properties.getToken())
                .timeout(properties.getTimeout())
                .retry(properties.getRetryCount(), properties.getRetryDelay())
                .build();
    }
}
