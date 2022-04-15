package com.jstarcraft.cloud.governance.zookeeper;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.x.discovery.ServiceDiscovery;
import org.apache.curator.x.discovery.ServiceDiscoveryBuilder;
import org.apache.curator.x.discovery.ServiceInstance;
import org.apache.curator.x.discovery.ServiceInstanceBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jstarcraft.cloud.governance.DefaultGovernanceInstance;
import com.jstarcraft.cloud.governance.GovernanceInstance;
import com.jstarcraft.cloud.governance.GovernanceManager;

/**
 * ZooKeeper治理管理器
 * 
 * @author Birdy
 *
 */
public class ZooKeeperGovernanceManager implements GovernanceManager {

    private static final Logger logger = LoggerFactory.getLogger(ZooKeeperGovernanceManager.class);

    private ServiceDiscovery<Map> zookeeper;

    public ZooKeeperGovernanceManager(CuratorFramework zookeeper, String path) {
        ServiceDiscoveryBuilder<Map> builder = ServiceDiscoveryBuilder.builder(Map.class);
        builder.client(zookeeper);
        builder.basePath(path);
        this.zookeeper = builder.build();
    }

    @Override
    public void registerInstance(GovernanceInstance instance) {
        try {
            ServiceInstanceBuilder<Map> builder = ServiceInstance.builder();
            builder.id(instance.getId());
            builder.name(instance.getCategory());
            builder.address(instance.getHost());
            builder.port(instance.getPort());
            builder.payload(instance.getMetadata());
            ServiceInstance<Map> information = builder.build();
            zookeeper.registerService(information);
        } catch (Exception exception) {
            throw new RuntimeException(exception);
        }
    }

    @Override
    public void deregisterInstance(GovernanceInstance instance) {
        try {
            ServiceInstanceBuilder<Map> builder = ServiceInstance.builder();
            builder.id(instance.getId());
            builder.name(instance.getCategory());
            builder.address(instance.getHost());
            builder.port(instance.getPort());
            builder.payload(instance.getMetadata());
            ServiceInstance<Map> information = builder.build();
            zookeeper.unregisterService(information);
        } catch (Exception exception) {
            throw new RuntimeException(exception);
        }

    }

    @Override
    public Set<String> discoverCategories() {
        try {
            Collection<String> categories = zookeeper.queryForNames();
            if (categories == null) {
                return Collections.emptySet();
            }
            return new LinkedHashSet<>(categories);
        } catch (Exception exception) {
            throw new RuntimeException(exception);
        }
    }

    @Override
    public void leaseInstance(GovernanceInstance instance) {
        // TODO Auto-generated method stub

    }

    @Override
    public List<GovernanceInstance> discoverInstances(String category) {
        try {
            Collection<ServiceInstance<Map>> informations = zookeeper.queryForInstances(category);
            List<GovernanceInstance> instances = new ArrayList<>(informations.size());
            for (ServiceInstance<Map> information : informations) {
                String id = information.getId();
                String host = information.getAddress();
                int port = information.getPort();
                Map<String, String> metadata = information.getPayload();
                GovernanceInstance instance = new DefaultGovernanceInstance(id, category, host, port, metadata);
                instances.add(instance);
            }
            return instances;
        } catch (Exception exception) {
            throw new RuntimeException(exception);
        }
    }

}
