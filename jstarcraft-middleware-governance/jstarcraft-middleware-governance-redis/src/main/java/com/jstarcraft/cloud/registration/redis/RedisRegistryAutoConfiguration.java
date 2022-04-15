package com.jstarcraft.cloud.registration.redis;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.cloud.client.serviceregistry.AutoServiceRegistrationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

@Configuration
@ConditionalOnProperty(value = "spring.redis.registry.enabled", matchIfMissing = true)
public class RedisRegistryAutoConfiguration {

    @Bean
    RedisServiceRegistry redisServiceRegistry() {
        return new RedisServiceRegistry();
    }

    @Bean
    RedisAutoServiceRegistration redisAutoServiceRegistration(RedisServiceRegistry redisServiceRegistry) {
        return new RedisAutoServiceRegistration(redisServiceRegistry, new AutoServiceRegistrationProperties());
    }

    @Bean
    @Primary
    RedisDiscoveryClient redisDiscoveryClient() {
        return new RedisDiscoveryClient();
    }

}
