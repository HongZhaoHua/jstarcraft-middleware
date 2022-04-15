package com.jstarcraft.cloud.registration.consul;

import java.util.Map;

import org.springframework.cloud.consul.discovery.ConsulServer;

import com.jstarcraft.cloud.registration.AbstractServerManager;
import com.netflix.client.config.IClientConfig;
import com.netflix.loadbalancer.ServerList;

/**
 * Consul服务管理器
 * 
 * @author Birdy
 *
 */
public class ConsulServerManager extends AbstractServerManager<ConsulServer> {

    public ConsulServerManager(IClientConfig clientConfig, ServerList<ConsulServer> serverList) {
        super(clientConfig, serverList);
    }

    @Override
    protected Map<String, String> getMetadata(ConsulServer server) {
        return server.getMetadata();
    }

}