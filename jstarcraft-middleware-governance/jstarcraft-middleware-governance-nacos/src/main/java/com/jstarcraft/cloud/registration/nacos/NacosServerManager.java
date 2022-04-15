package com.jstarcraft.cloud.registration.nacos;

import java.util.Map;

import com.alibaba.cloud.nacos.ribbon.NacosServer;
import com.jstarcraft.cloud.registration.AbstractServerManager;
import com.netflix.client.config.IClientConfig;
import com.netflix.loadbalancer.ServerList;

/**
 * Nacos服务管理器
 * 
 * @author Birdy
 *
 */
public class NacosServerManager extends AbstractServerManager<NacosServer> {

    public NacosServerManager(IClientConfig clientConfig, ServerList<NacosServer> serverList) {
        super(clientConfig, serverList);
    }

    @Override
    protected Map<String, String> getMetadata(NacosServer server) {
        return server.getMetadata();
    }

}