package com.jstarcraft.cloud.registration.redis;

import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.test.context.junit4.SpringRunner;

import redis.embedded.RedisServer;

@RunWith(SpringRunner.class)
@SpringBootTest
public class RedisServerManagerTestCase {

    @Autowired
    private RedisServiceRegistry registry;

    @Autowired
    private RedisRegistration registration;

    @Autowired
    private LoadBalancerClient balancer;

    @Autowired
    private DiscoveryClient discovery;

    private static RedisServer redis;

    @BeforeClass
    public static void startRedis() {
        redis = RedisServer.builder().port(6379).setting("maxmemory 64M").build();
        redis.start();
    }

    @AfterClass
    public static void stopRedis() {
        redis.stop();
    }

    @Test
    public void testChoose() throws Exception {
        String serviceId = "demo";

        // 服务注册
        registry.register(registration);
        // 服务发现
        while (discovery.getInstances(serviceId).size() == 0) {
            Thread.sleep(1000L);
        }
        ServiceInstance instance = balancer.choose(serviceId);
        Assert.assertNotNull(instance);
        Assert.assertEquals(serviceId, instance.getServiceId());
        // 服务注销
        registry.deregister(registration);
    }

}
