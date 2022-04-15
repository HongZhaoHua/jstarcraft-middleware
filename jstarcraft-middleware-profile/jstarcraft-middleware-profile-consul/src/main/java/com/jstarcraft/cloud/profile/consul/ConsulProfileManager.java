package com.jstarcraft.cloud.profile.consul;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ecwid.consul.v1.ConsulClient;
import com.ecwid.consul.v1.QueryParams;
import com.ecwid.consul.v1.Response;
import com.ecwid.consul.v1.kv.model.GetValue;
import com.jstarcraft.cloud.profile.ProfileManager;
import com.jstarcraft.cloud.profile.ProfileMonitor;
import com.jstarcraft.core.common.configuration.Configurator;
import com.jstarcraft.core.common.configuration.string.JsonConfigurator;
import com.jstarcraft.core.common.configuration.string.PropertyConfigurator;
import com.jstarcraft.core.common.configuration.string.XmlConfigurator;
import com.jstarcraft.core.common.configuration.string.YamlConfigurator;

/**
 * Consul配置管理器
 * 
 * @author Birdy
 *
 */
public class ConsulProfileManager implements ProfileManager {

    private static final Logger logger = LoggerFactory.getLogger(ConsulProfileManager.class);

    private ConsulClient consul;

    private String format;

    public ConsulProfileManager(ConsulClient consul, String format) {
        this.consul = consul;
        this.format = format;
    }

    @Override
    public Configurator getConfiguration(String name) {
        Response<GetValue> response = consul.getKVValue(name, QueryParams.DEFAULT);
        GetValue keyValue = response.getValue();
        String content = keyValue.getDecodedValue();
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
