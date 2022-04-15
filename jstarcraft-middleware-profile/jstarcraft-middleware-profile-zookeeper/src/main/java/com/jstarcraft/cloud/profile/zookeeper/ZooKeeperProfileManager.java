package com.jstarcraft.cloud.profile.zookeeper;

import org.apache.curator.framework.CuratorFramework;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jstarcraft.cloud.profile.ProfileManager;
import com.jstarcraft.cloud.profile.ProfileMonitor;
import com.jstarcraft.core.common.configuration.Configurator;
import com.jstarcraft.core.common.configuration.string.JsonConfigurator;
import com.jstarcraft.core.common.configuration.string.PropertyConfigurator;
import com.jstarcraft.core.common.configuration.string.XmlConfigurator;
import com.jstarcraft.core.common.configuration.string.YamlConfigurator;
import com.jstarcraft.core.utility.StringUtility;

/**
 * ZooKeeper配置管理器
 * 
 * @author Birdy
 *
 */
public class ZooKeeperProfileManager implements ProfileManager {

    private static final Logger logger = LoggerFactory.getLogger(ZooKeeperProfileManager.class);

    private CuratorFramework zookeeper;

    private String format;

    private String path;

    public ZooKeeperProfileManager(CuratorFramework zookeeper, String format, String path) {
        this.zookeeper = zookeeper;
        this.format = format;
        this.path = path;
    }

    @Override
    public Configurator getConfiguration(String name) {
        try {
            byte[] data = zookeeper.getData().forPath(path + "/" + name);
            String content = new String(data, StringUtility.CHARSET);
            switch (format) {
            case "json":
                return new JsonConfigurator(content);
            case "properties":
                return new PropertyConfigurator(content);
            case "xml":
                return new XmlConfigurator(content);
            case "yaml":
                return new YamlConfigurator(content);
            }
            throw new IllegalArgumentException();
        } catch (Exception exception) {
            throw new RuntimeException(exception);
        }
    }

    @Override
    public void registerMonitor(String name, ProfileMonitor monitor) {
        // TODO Auto-generated method stub

    }

    @Override
    public void unregisterMonitor(String name, ProfileMonitor monitor) {
        // TODO Auto-generated method stub

    }

}
