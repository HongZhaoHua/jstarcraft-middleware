package com.jstarcraft.cloud.configuration.redis;

import org.redisson.Redisson;

import com.jstarcraft.cloud.configuration.ConfigurationManager;
import com.jstarcraft.cloud.configuration.ConfigurationMonitor;
import com.jstarcraft.core.common.configuration.Configurator;

/**
 * Redis配置管理器
 * 
 * @author Birdy
 *
 */
public class RedisConfigurationManager implements ConfigurationManager {

    private Redisson redisson;

    @Override
    public Configurator getConfiguration(String name) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void registerMonitor(String name, ConfigurationMonitor monitor) {
        // TODO Auto-generated method stub

    }

    @Override
    public void unregisterMonitor(String name, ConfigurationMonitor monitor) {
        // TODO Auto-generated method stub

    }

}
