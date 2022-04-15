package com.jstarcraft.cloud.registration.redis;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.serviceregistry.AbstractAutoServiceRegistration;
import org.springframework.cloud.client.serviceregistry.AutoServiceRegistrationProperties;
import org.springframework.cloud.client.serviceregistry.ServiceRegistry;

public class RedisAutoServiceRegistration extends AbstractAutoServiceRegistration<RedisRegistration> {

    @Autowired
    private RedisRegistration redisRegistration;

    protected RedisAutoServiceRegistration(ServiceRegistry<RedisRegistration> serviceRegistry, AutoServiceRegistrationProperties properties) {
        super(serviceRegistry, properties);
    }

    @Override
    protected Object getConfiguration() {
        return null;
    }

    @Override
    protected boolean isEnabled() {
        return true;
    }

    @Override
    protected RedisRegistration getRegistration() {
        return redisRegistration;
    }

    @Override
    protected RedisRegistration getManagementRegistration() {
        return null;
    }

}
