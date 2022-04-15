package com.jstarcraft.cloud.registration.configurer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.cloud.consul.discovery.ConsulDiscoveryProperties;
import org.springframework.cloud.consul.discovery.ConsulRibbonClientConfiguration;
import org.springframework.cloud.consul.discovery.ConsulServerList;
import org.springframework.context.annotation.Bean;

import com.ecwid.consul.v1.ConsulClient;
import com.jstarcraft.cloud.registration.consul.ConsulServerManager;
import com.netflix.client.config.IClientConfig;
import com.netflix.loadbalancer.ServerList;

// 注释@Configuration,不能被@ComponentScan自动扫描,但能被@RibbonClients或者@RibbonClient手动扫描
@AutoConfigureAfter(ConsulRibbonClientConfiguration.class)
// 此类是为了演示如何覆盖Spring Cloud的自动装配
public class ConsulLoadBalanceConfigurer {

    @Autowired
    private ConsulClient client;

    @Bean
    public ServerList<?> ribbonServerList(IClientConfig config, ConsulDiscoveryProperties properties) {
        ConsulServerList serverList = new ConsulServerList(client, properties);
        serverList.initWithNiwsConfig(config);
        ConsulServerManager manager = new ConsulServerManager(config, serverList);
        return manager;
    }

}