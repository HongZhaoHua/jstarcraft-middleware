package com.jstarcraft.cloud.profile.config;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.web.client.RestTemplate;

import com.jstarcraft.core.common.configuration.Configurator;

public class ConfigProfileManagerTestCase {

    @Test
    public void test() {
        RestTemplate config = new RestTemplate();
        ConfigProfileManager manager = new ConfigProfileManager(config, "json", "http://localhost:8888", "test", "master");
        Configurator configurator = manager.getConfiguration("jstarcraft");
        Assert.assertEquals("random", configurator.getString("race"));
    }

}
