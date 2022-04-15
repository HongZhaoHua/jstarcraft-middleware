package com.jstarcraft.cloud.registration.nacos;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.test.context.junit4.SpringRunner;

import com.alibaba.cloud.nacos.registry.NacosRegistration;
import com.alibaba.cloud.nacos.registry.NacosServiceRegistry;

@RunWith(SpringRunner.class)
@SpringBootTest
public class NacosServerManagerTestCase {

    @Autowired
    private NacosServiceRegistry registry;

    @Autowired
    private NacosRegistration registration;

    @Autowired
    private LoadBalancerClient balancer;

    @Autowired
    private DiscoveryClient discovery;

    @Test
    public void testChoose() throws Exception {
        String serviceId = "demo";

        // 单元测试时,由于不会触发WebServerStartStopLifecycle,导致NacosDiscoveryProperties的port永远为-1
        // 所以会导致instance format invalid: Your IP address is spelled incorrectly
        registration.setPort(8080);
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
