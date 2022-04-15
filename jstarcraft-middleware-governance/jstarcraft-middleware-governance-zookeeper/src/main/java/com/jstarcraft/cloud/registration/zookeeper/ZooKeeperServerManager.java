package com.jstarcraft.cloud.registration.zookeeper;

import java.util.Map;

import org.springframework.cloud.zookeeper.discovery.ZookeeperServer;

import com.jstarcraft.cloud.registration.AbstractServerManager;
import com.netflix.client.config.IClientConfig;
import com.netflix.loadbalancer.ServerList;

/**
 * ZooKeeper服务管理器
 * 
 * @author Birdy
 *
 */
public class ZooKeeperServerManager extends AbstractServerManager<ZookeeperServer> {

    public ZooKeeperServerManager(IClientConfig clientConfig, ServerList<ZookeeperServer> serverList) {
        super(clientConfig, serverList);
    }

    @Override
    protected Map<String, String> getMetadata(ZookeeperServer server) {
        return server.getInstance().getPayload().getMetadata();
    }

}