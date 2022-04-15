package com.jstarcraft.cloud.profile.nacos;

import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import com.alibaba.nacos.api.config.ConfigFactory;
import com.alibaba.nacos.api.config.ConfigService;
import com.jstarcraft.core.common.configuration.Configurator;

public class NacosProfileManagerTestCase {

    private static ConfigService nacos;

    @BeforeClass
    public static void start() throws Exception {
        nacos = ConfigFactory.createConfigService("127.0.0.1:8848");
    }

    @AfterClass
    public static void stop() throws Exception {
    }

    @Test
    public void test() throws Exception {
        nacos.publishConfig("jstarcraft", "group", "race=random");
        Thread.sleep(5000L);
        NacosProfileManager manager = new NacosProfileManager(nacos, "properties");
        Configurator configurator = manager.getConfiguration("jstarcraft");
        Assert.assertEquals("random", configurator.getString("race"));
        nacos.removeConfig("jstarcraft", "group");
    }

}
