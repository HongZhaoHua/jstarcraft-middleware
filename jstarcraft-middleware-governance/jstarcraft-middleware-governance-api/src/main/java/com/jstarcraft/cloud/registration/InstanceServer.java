package com.jstarcraft.cloud.registration;

import java.util.Map;

import org.springframework.cloud.client.ServiceInstance;

import com.netflix.loadbalancer.Server;

/**
 * 实例服务
 * 
 * <pre>
 * 用于将ServiceInstance适配到Ribbon
 * </pre>
 * 
 * @author Birdy
 *
 */
public class InstanceServer extends Server {

    protected ServiceInstance instance;

    public InstanceServer(ServiceInstance instance) {
        super(instance.getScheme(), instance.getHost(), instance.getPort());
        this.instance = instance;
    }

    public Map<String, String> getMetadata() {
        return instance.getMetadata();
    }

}
