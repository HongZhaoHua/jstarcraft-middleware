package com.jstarcraft.cloud.profile;

import com.jstarcraft.core.common.configuration.Configurator;

/**
 * 配置管理器
 * 
 * @author Birdy
 *
 */
public interface ProfileManager {

    /**
     * 获取配置
     * 
     * @param name
     * @return
     */
    Configurator getConfiguration(String name);

    /**
     * 注册监控器
     * 
     * @param name
     * @param monitor
     */
    void registerMonitor(String name, ProfileMonitor monitor);

    /**
     * 注销监控器
     * 
     * @param name
     * @param monitor
     */
    void unregisterMonitor(String name, ProfileMonitor monitor);

}
