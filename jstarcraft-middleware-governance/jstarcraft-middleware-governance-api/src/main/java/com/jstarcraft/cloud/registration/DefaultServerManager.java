package com.jstarcraft.cloud.registration;

import java.util.ArrayList;
import java.util.List;

import org.springframework.cloud.client.ServiceInstance;

import com.netflix.client.config.IClientConfig;

/**
 * 默认服务管理器
 * 
 * @author Birdy
 *
 */
public class DefaultServerManager implements ServerManager<InstanceServer> {

    protected String serviceId;

    protected DiscoveryManager discoveryManager;

    public DefaultServerManager(IClientConfig clientConfig, DiscoveryManager discoveryManager) {
        this.serviceId = clientConfig.getClientName();
        this.discoveryManager = discoveryManager;
    }

    @Override
    public String getServiceId() {
        return serviceId;
    }

    private List<InstanceServer> getServers() {
        List<ServiceInstance> instances = discoveryManager.getInstances(serviceId);
        List<InstanceServer> servers = new ArrayList<>(instances.size());
        for (ServiceInstance instance : instances) {
            InstanceServer server = new InstanceServer(instance);
            servers.add(server);
        }
        return servers;
    }

    @Override
    public List<InstanceServer> getInitialListOfServers() {
        List<InstanceServer> servers = getServers();
        return servers;
    }

    @Override
    public List<InstanceServer> getUpdatedListOfServers() {
        List<InstanceServer> servers = getServers();
        return servers;
    }

}