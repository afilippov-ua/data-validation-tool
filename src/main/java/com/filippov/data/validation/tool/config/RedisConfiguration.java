/*
 *   Copyright 2018-2020 the original author or authors.
 *  
 *   Licensed under the Apache License, Version 2.0 (the "License");
 *   you may not use this file except in compliance with the License.
 *   You may obtain a copy of the License at
 *  
 *        https://www.apache.org/licenses/LICENSE-2.0
 *  
 *   Unless required by applicable law or agreed to in writing, software
 *   distributed under the License is distributed on an "AS IS" BASIS,
 *   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *   See the License for the specific language governing permissions and
 *   limitations under the License.
 */

package com.filippov.data.validation.tool.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;

@Slf4j
@Configuration
public class RedisConfiguration {

    @Bean
    public JedisConnectionFactory jedisConnectionFactory(DataValidationToolProperties applicationProperties) {
        DataValidationToolProperties.RedisConfig config = applicationProperties.getRedisConfig();
        if (config == null
                || config.getHost() == null
                || config.getPort() == null) {
            config = new DataValidationToolProperties.RedisConfig();
            config.setHost("localhost");
            config.setPort(6379);
            log.debug("Redis configuration is not set. Using the default one: '{}:{}'", config.getHost(), config.getPort());
        }
        final RedisStandaloneConfiguration standaloneConfiguration = new RedisStandaloneConfiguration();
        standaloneConfiguration.setHostName(config.getHost());
        standaloneConfiguration.setPort(config.getPort());

        log.debug("Redis configuration: '{}:{}'", config.getHost(), config.getPort());
        return new JedisConnectionFactory(standaloneConfiguration);
    }

    @Bean
    public RedisTemplate<String, Object> redisTemplate(DataValidationToolProperties applicationProperties) {
        RedisTemplate<String, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(jedisConnectionFactory(applicationProperties));
        return template;
    }
}
