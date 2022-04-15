package com.jstarcraft.cloud.governance.ribbon;

import java.util.Map;

import com.jstarcraft.cloud.governance.GovernanceInstance;
import com.netflix.loadbalancer.Server;

/**
 * 治理服务
 * 
 * <pre>
 * 用于将GovernanceInstance适配到Ribbon
 * </pre>
 * 
 * @author Birdy
 *
 */
public class GovernanceServer extends Server {

    protected GovernanceInstance instance;

    public GovernanceServer(GovernanceInstance instance) {
        super(instance.getHost(), instance.getPort());
        this.instance = instance;
    }

    public Map<String, String> getMetadata() {
        return instance.getMetadata();
    }

}
