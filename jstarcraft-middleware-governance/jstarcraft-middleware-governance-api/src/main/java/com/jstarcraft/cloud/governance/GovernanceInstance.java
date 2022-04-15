package com.jstarcraft.cloud.governance;

import java.util.Map;

/**
 * 治理实例
 * 
 * @author Birdy
 *
 */
public interface GovernanceInstance {

    /**
     * 获取实例标识
     * 
     * @return
     */
    String getId();

    /**
     * 获取实例类别
     * 
     * @return
     */
    String getCategory();

    /**
     * 获取实例域名
     * 
     * @return
     */
    String getHost();

    /**
     * 获取实例端口
     * 
     * @return
     */
    int getPort();

    /**
     * 获取实例元信息
     * 
     * @return
     */
    Map<String, String> getMetadata();

    // TODO 准备支持获取实例状态
    // getState();

}
