package com.jstarcraft.cloud.configuration;

import com.jstarcraft.core.common.configuration.Configurator;

/**
 * 配置监控器
 * 
 * @author Birdy
 *
 */
public interface ConfigurationMonitor {

    /**
     * 变更
     * 
     * @param name
     * @param from
     * @param to
     */
    void change(String name, Configurator from, Configurator to);

}
