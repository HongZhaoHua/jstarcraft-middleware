package com.jstarcraft.cloud.governance;

import java.util.List;
import java.util.Set;

/**
 * 治理管理器
 * 
 * @author Birdy
 *
 */
public interface GovernanceManager {

    /**
     * 注册实例
     * 
     * @param instance
     */
    void registerInstance(GovernanceInstance instance);

    /**
     * 续租实例
     * 
     * @param instance
     */
    void leaseInstance(GovernanceInstance instance);

    /**
     * 注销实例
     * 
     * @param instance
     */
    void deregisterInstance(GovernanceInstance instance);

    /**
     * 发现类别
     * 
     * @return
     */
    Set<String> discoverCategories();

    /**
     * 发现实例
     * 
     * @param category
     * @return
     */
    List<GovernanceInstance> discoverInstances(String category);

}
