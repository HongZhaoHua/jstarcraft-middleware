package com.jstarcraft.cloud.registration.redis;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.data.redis.core.StringRedisTemplate;

public class RedisDiscoveryClient implements DiscoveryClient {

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Override
    public String description() {
        return "redis注册中心的服务发现";
    }

    @Override
    public List<ServiceInstance> getInstances(String serviceId) {
        List<String> strings = redisTemplate.opsForList().range(serviceId, 0, -1);
        return strings.parallelStream().map((Function<String, ServiceInstance>) s -> {
            RedisRegistration redisRegistration = new RedisRegistration();
            redisRegistration.setApplicationName(serviceId);
            String hostName = StringUtils.split(s, ":")[0];
            String port = StringUtils.split(s, ":")[1];
            redisRegistration.setHost(hostName);
            redisRegistration.setPort(Integer.parseInt(port));
            // redisRegistration
            return redisRegistration;
        }).collect(Collectors.toList());

    }

    @Override
    public List<String> getServices() {
        List<String> list = new ArrayList<>();
        list.addAll(redisTemplate.keys("*"));
        return list;
    }

}
