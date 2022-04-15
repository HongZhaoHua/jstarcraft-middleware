package com.jstarcraft.cloud.configuration.zookeeper;

import org.apache.curator.framework.CuratorFramework;

import com.jstarcraft.cloud.configuration.ConfigurationManager;
import com.jstarcraft.cloud.configuration.ConfigurationMonitor;
import com.jstarcraft.core.common.configuration.Configurator;

/**
 * ZooKeeper配置管理器
 * 
 * @author Birdy
 *
 */
public class ZooKeeperConfigurationManager implements ConfigurationManager {

    private String path;

    private CuratorFramework curator;

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
