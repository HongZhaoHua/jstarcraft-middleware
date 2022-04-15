package com.jstarcraft.cloud.registration.configure;

import org.apache.curator.x.discovery.ServiceDiscovery;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.cloud.zookeeper.discovery.ZookeeperInstance;
import org.springframework.cloud.zookeeper.discovery.ZookeeperRibbonClientConfiguration;
import org.springframework.cloud.zookeeper.discovery.ZookeeperServerList;
import org.springframework.cloud.zookeeper.discovery.dependency.ZookeeperDependencies;
import org.springframework.context.annotation.Bean;

import com.jstarcraft.cloud.registration.zookeeper.ZooKeeperServerManager;
import com.netflix.client.config.IClientConfig;
import com.netflix.loadbalancer.ServerList;

// 注释@Configuration,不能被@ComponentScan自动扫描,但能被@RibbonClients或者@RibbonClient手动扫描
@AutoConfigureAfter(ZookeeperRibbonClientConfiguration.class)
// 此类是为了演示如何覆盖Spring Cloud的自动装配
public class ZooKeeperLoadBalanceConfigurer {

    @Bean
    public ServerList<?> ribbonServerList(IClientConfig config, ZookeeperDependencies dependencies, ServiceDiscovery<ZookeeperInstance> discovery) {
        ZookeeperServerList serverList = new ZookeeperServerList(discovery);
        serverList.initFromDependencies(config, dependencies);
        ZooKeeperServerManager manager = new ZooKeeperServerManager(config, serverList);
        return manager;
    }

    @Bean
    public ZookeeperDependencies zookeeperDependencies() {
        return new ZookeeperDependencies();
    }

}