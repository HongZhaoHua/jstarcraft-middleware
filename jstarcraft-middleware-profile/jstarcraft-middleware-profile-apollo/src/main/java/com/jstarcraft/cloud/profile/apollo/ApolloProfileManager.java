package com.jstarcraft.cloud.profile.apollo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ctrip.framework.apollo.ConfigFile;
import com.ctrip.framework.apollo.ConfigService;
import com.ctrip.framework.apollo.core.enums.ConfigFileFormat;
import com.jstarcraft.cloud.profile.ProfileManager;
import com.jstarcraft.cloud.profile.ProfileMonitor;
import com.jstarcraft.core.common.configuration.Configurator;
import com.jstarcraft.core.common.configuration.string.JsonConfigurator;
import com.jstarcraft.core.common.configuration.string.PropertyConfigurator;
import com.jstarcraft.core.common.configuration.string.XmlConfigurator;
import com.jstarcraft.core.common.configuration.string.YamlConfigurator;

/**
 * Apollo配置管理器
 * 
 * @author Birdy
 *
 */
public class ApolloProfileManager implements ProfileManager {

    private static final Logger logger = LoggerFactory.getLogger(ApolloProfileManager.class);

    private ConfigFileFormat format;

    public ApolloProfileManager(ConfigFileFormat format) {
        this.format = format;
    }

    @Override
    public Configurator getConfiguration(String name) {
        ConfigFile config = ConfigService.getConfigFile(name, format);
        String content = config.getContent();
        switch (format) {
        case JSON:
            return new JsonConfigurator(content);
        case Properties:
            return new PropertyConfigurator(content);
        case XML:
            return new XmlConfigurator(content);
        case YAML:
            return new YamlConfigurator(content);
        default:
            throw new IllegalArgumentException();
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
