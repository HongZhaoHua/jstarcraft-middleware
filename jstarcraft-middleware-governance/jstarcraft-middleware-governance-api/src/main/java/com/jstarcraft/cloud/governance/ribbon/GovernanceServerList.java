package com.jstarcraft.cloud.governance.ribbon;

import java.util.ArrayList;
import java.util.List;

import com.jstarcraft.cloud.governance.GovernanceInstance;
import com.jstarcraft.cloud.governance.GovernanceManager;
import com.netflix.client.config.IClientConfig;
import com.netflix.loadbalancer.ServerList;

public class GovernanceServerList implements ServerList<GovernanceServer> {

    protected String serviceId;

    protected GovernanceManager discoveryManager;

    public GovernanceServerList(IClientConfig clientConfig, GovernanceManager discoveryManager) {
        this.serviceId = clientConfig.getClientName();
        this.discoveryManager = discoveryManager;
    }

    protected List<GovernanceServer> getServers() {
        List<GovernanceInstance> instances = discoveryManager.discoverInstances(serviceId);
        List<GovernanceServer> servers = new ArrayList<>(instances.size());
        for (GovernanceInstance instance : instances) {
            GovernanceServer server = new GovernanceServer(instance);
            servers.add(server);
        }
        return servers;
    }

    @Override
    public List<GovernanceServer> getInitialListOfServers() {
        List<GovernanceServer> servers = getServers();
        return servers;
    }

    @Override
    public List<GovernanceServer> getUpdatedListOfServers() {
        List<GovernanceServer> servers = getServers();
        return servers;
    }

    public String getServiceId() {
        return serviceId;
    }

}
