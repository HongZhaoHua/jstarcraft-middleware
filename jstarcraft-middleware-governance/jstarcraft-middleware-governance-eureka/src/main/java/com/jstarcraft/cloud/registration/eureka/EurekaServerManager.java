package com.jstarcraft.cloud.registration.eureka;

import java.util.Map;

import com.jstarcraft.cloud.registration.AbstractServerManager;
import com.netflix.client.config.IClientConfig;
import com.netflix.loadbalancer.ServerList;
import com.netflix.niws.loadbalancer.DiscoveryEnabledServer;

/**
 * Eureka服务管理器
 * 
 * @author Birdy
 *
 */
public class EurekaServerManager extends AbstractServerManager<DiscoveryEnabledServer> {

    public EurekaServerManager(IClientConfig clientConfig, ServerList<DiscoveryEnabledServer> serverList) {
        super(clientConfig, serverList);
    }

    @Override
    protected Map<String, String> getMetadata(DiscoveryEnabledServer server) {
        return server.getInstanceInfo().getMetadata();
    }

}