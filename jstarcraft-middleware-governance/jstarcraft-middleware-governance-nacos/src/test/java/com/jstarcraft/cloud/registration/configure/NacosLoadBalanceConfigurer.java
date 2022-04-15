package com.jstarcraft.cloud.registration.configure;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.cloud.netflix.ribbon.PropertiesFactory;
import org.springframework.context.annotation.Bean;

import com.alibaba.cloud.nacos.NacosDiscoveryProperties;
import com.alibaba.cloud.nacos.ribbon.NacosRibbonClientConfiguration;
import com.alibaba.cloud.nacos.ribbon.NacosServerList;
import com.jstarcraft.cloud.registration.nacos.NacosServerManager;
import com.netflix.client.config.IClientConfig;
import com.netflix.loadbalancer.ServerList;

// 注释@Configuration,不能被@ComponentScan自动扫描,但能被@RibbonClients或者@RibbonClient手动扫描
@AutoConfigureAfter(NacosRibbonClientConfiguration.class)
// 此类是为了演示如何覆盖Spring Cloud的自动装配
public class NacosLoadBalanceConfigurer {

    @Autowired
    private PropertiesFactory propertiesFactory;

    @Bean
    public ServerList<?> ribbonServerList(IClientConfig config, NacosDiscoveryProperties properties) {
        NacosServerList serverList = new NacosServerList(properties);
        serverList.initWithNiwsConfig(config);
        NacosServerManager manager = new NacosServerManager(config, serverList);
        return manager;
    }

}