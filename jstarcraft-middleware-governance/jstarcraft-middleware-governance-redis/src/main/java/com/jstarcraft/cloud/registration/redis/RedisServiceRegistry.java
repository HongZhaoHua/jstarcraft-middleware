package com.jstarcraft.cloud.registration.redis;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.serviceregistry.ServiceRegistry;
import org.springframework.data.redis.core.StringRedisTemplate;

public class RedisServiceRegistry implements ServiceRegistry<RedisRegistration> {

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Override
    public void register(RedisRegistration registration) {
        String serviceId = registration.getServiceId();
        redisTemplate.opsForList().leftPush(serviceId, registration.getHost() + ":" + registration.getPort());

    }

    @Override
    public void deregister(RedisRegistration registration) {
        redisTemplate.opsForList().remove(registration.getServiceId(), 1, registration.getHost() + ":" + registration.getPort());
    }

    @Override
    public void close() {
    }

    @Override
    public void setStatus(RedisRegistration registration, String status) {
    }

    @Override
    public <T> T getStatus(RedisRegistration registration) {
        return null;
    }

}
