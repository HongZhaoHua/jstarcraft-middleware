package com.jstarcraft.cloud.registration.configurer;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.jstarcraft.cloud.registration.DefaultServerManager;
import com.jstarcraft.cloud.registration.DiscoveryManager;
import com.netflix.client.config.IClientConfig;
import com.netflix.loadbalancer.ServerList;

@Configuration
public class RedisLoadBalanceConfigurer {

    @Bean
    public ServerList<?> ribbonServerList(IClientConfig config, DiscoveryManager discoveryManager) {
        DefaultServerManager manager = new DefaultServerManager(config, discoveryManager);
        return manager;
    }

}