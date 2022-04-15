package com.jstarcraft.cloud.configuration.nacos;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.alibaba.nacos.api.config.ConfigService;
import com.alibaba.nacos.api.config.listener.Listener;
import com.alibaba.nacos.api.exception.NacosException;
import com.jstarcraft.cloud.configuration.ConfigurationManager;
import com.jstarcraft.cloud.configuration.ConfigurationMonitor;
import com.jstarcraft.core.common.configuration.Configurator;

/**
 * Nacos配置管理器
 * 
 * @author Birdy
 *
 */
public class NacosConfigurationManager implements ConfigurationManager {

    private String data;

    private String group;

    private ConfigService nacos;

    private Map<ConfigurationMonitor, Listener> monitors = new HashMap<>();

    private ExecutorService executor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());

    public String getString(String name) {
        try {
            return nacos.getConfig(data, group, 1000L);
        } catch (NacosException exception) {
            throw new RuntimeException(exception);
        }
    }

    @Override
    public Configurator getConfiguration(String name) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public synchronized void registerMonitor(String name, ConfigurationMonitor monitor) {
        try {
            Listener configListener = new Listener() {

                @Override
                public void receiveConfigInfo(String config) {
                    // TODO
                    monitor.change(data, null, null);
                }

                @Override
                public Executor getExecutor() {
                    return executor;
                }

            };
            nacos.addListener(data, group, configListener);
        } catch (NacosException exception) {
            throw new RuntimeException(exception);
        }
    }

    @Override
    public synchronized void unregisterMonitor(String name, ConfigurationMonitor monitor) {
        Listener listener = monitors.remove(monitor);
        if (listener != null) {
            nacos.removeListener(data, group, listener);
        }
    }

}
