package com.jstarcraft.cloud.profile;

import com.jstarcraft.core.common.configuration.Configurator;

/**
 * 配置监控器
 * 
 * @author Birdy
 *
 */
public interface ProfileMonitor {

    /**
     * 变更
     * 
     * @param name
     * @param from
     * @param to
     */
    void changeProfile(String name, Configurator from, Configurator to);

}
