package com.jstarcraft.cloud.configuration.apollo;

import java.util.HashMap;
import java.util.Map;

import com.ctrip.framework.apollo.Config;
import com.ctrip.framework.apollo.ConfigChangeListener;
import com.ctrip.framework.apollo.ConfigService;
import com.ctrip.framework.apollo.model.ConfigChangeEvent;
import com.jstarcraft.cloud.configuration.ConfigurationManager;
import com.jstarcraft.cloud.configuration.ConfigurationMonitor;
import com.jstarcraft.core.common.configuration.Configurator;
import com.jstarcraft.core.common.configuration.string.MapConfigurator;

/**
 * Apollo配置管理器
 * 
 * @author Birdy
 *
 */
public class ApolloConfigurationManager implements ConfigurationManager {

    private Map<ConfigurationMonitor, ConfigChangeListener> monitors = new HashMap<>();

    @Override
    public Configurator getConfiguration(String name) {
        Config apollo = ConfigService.getConfig(name);
        HashMap<String, String> keyValues = new HashMap<>();
        for (String key : apollo.getPropertyNames()) {
            String value = apollo.getProperty(key, null);
            keyValues.put(key, value);
        }
        Configurator configuration = new MapConfigurator(keyValues);
        return configuration;
    }

    @Override
    public synchronized void registerMonitor(String name, ConfigurationMonitor monitor) {
        ConfigChangeListener listener = new ConfigChangeListener() {

            @Override
            public void onChange(ConfigChangeEvent event) {
                String name = event.getNamespace();
                // TODO 此处需要重构
                monitor.change(name, null, null);
            }

        };
        if (monitors.putIfAbsent(monitor, listener) == null) {
//            apollo.apollo.addChangeListener(listener);
        }
    }

    @Override
    public synchronized void unregisterMonitor(String name, ConfigurationMonitor monitor) {
        ConfigChangeListener listener = monitors.remove(monitor);
        if (listener != null) {
//            apollo.removeChangeListener(listener);
        }
    }

}
