package com.jstarcraft.cloud.governance.kubernetes;

import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jstarcraft.cloud.governance.GovernanceInstance;
import com.jstarcraft.cloud.governance.GovernanceManager;

import io.fabric8.kubernetes.client.KubernetesClient;

/**
 * Kubernetes治理管理器
 * 
 * @author Birdy
 *
 */
public class KubernetesGovernanceManager implements GovernanceManager {

    private static final Logger logger = LoggerFactory.getLogger(KubernetesGovernanceManager.class);

    private KubernetesClient kubernetes;

    @Override
    public void registerInstance(GovernanceInstance instance) {
        // TODO Auto-generated method stub

    }

    @Override
    public void deregisterInstance(GovernanceInstance instance) {
        // TODO Auto-generated method stub

    }

    @Override
    public void leaseInstance(GovernanceInstance instance) {
        // TODO Auto-generated method stub

    }

    @Override
    public Set<String> discoverCategories() {
        return null;
    }

    @Override
    public List<GovernanceInstance> discoverInstances(String category) {
        // TODO Auto-generated method stub
        return null;
    }

}
