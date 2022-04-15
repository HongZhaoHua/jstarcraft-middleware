package com.jstarcraft.cloud.profile.redis;

import org.redisson.Redisson;
import org.redisson.api.RBucket;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jstarcraft.cloud.profile.ProfileManager;
import com.jstarcraft.cloud.profile.ProfileMonitor;
import com.jstarcraft.core.common.configuration.Configurator;
import com.jstarcraft.core.common.configuration.string.JsonConfigurator;
import com.jstarcraft.core.common.configuration.string.PropertyConfigurator;
import com.jstarcraft.core.common.configuration.string.XmlConfigurator;
import com.jstarcraft.core.common.configuration.string.YamlConfigurator;

/**
 * Redis配置管理器
 * 
 * @author Birdy
 *
 */
public class RedisProfileManager implements ProfileManager {

    private static final Logger logger = LoggerFactory.getLogger(RedisProfileManager.class);

    private Redisson redis;

    private String format;

    public RedisProfileManager(Redisson redis, String format) {
        this.redis = redis;
        this.format = format;
    }

    @Override
    public Configurator getConfiguration(String name) {
        RBucket<String> bucket = redis.getBucket(name);
        String content = bucket.get();
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
