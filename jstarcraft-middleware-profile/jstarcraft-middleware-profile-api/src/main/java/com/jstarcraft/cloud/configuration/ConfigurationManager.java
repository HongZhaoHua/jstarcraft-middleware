package com.jstarcraft.cloud.configuration;

import com.jstarcraft.core.common.configuration.Configurator;

/**
 * 配置管理器
 * 
 * @author Birdy
 *
 */
public interface ConfigurationManager {

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
    void registerMonitor(String name, ConfigurationMonitor monitor);

    /**
     * 注销监控器
     * 
     * @param name
     * @param monitor
     */
    void unregisterMonitor(String name, ConfigurationMonitor monitor);

}
