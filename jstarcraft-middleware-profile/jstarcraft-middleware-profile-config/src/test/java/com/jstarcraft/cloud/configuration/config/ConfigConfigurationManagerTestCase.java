package com.jstarcraft.cloud.configuration.config;

import java.util.Collection;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.reactive.function.client.WebClient;

import com.jstarcraft.core.common.configuration.Configurator;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ConfigConfigurationManagerTestCase {

    @Autowired
    private ConfigConfigurationManager configurationManager;

    @Autowired
    private ConfigProperties properties;

    /**
     * 测试刷新
     */
    @Test
    public void testRefresh() {
        Assert.assertEquals("old-config", properties.getConfig());
        {
            Configurator configuration = configurationManager.getConfiguration("config");
            Assert.assertEquals("old-config", configuration.getString("mock.config"));
        }

        WebClient webClient = WebClient.create();
        {
            // 设置新配置
            webClient.get()
                    // 设置URL
                    .uri("http://localhost:8888/config/update2New").retrieve().bodyToMono(String.class).block();
        }

        {
            // 触发刷新配置
            Collection<String> changes = configurationManager.refresh();
            Assert.assertTrue(changes.contains("mock.config"));
        }

        Assert.assertEquals("new-config", properties.getConfig());
        {
            Configurator configuration = configurationManager.getConfiguration("config");
            Assert.assertEquals("new-config", configuration.getString("mock.config"));
        }

        {
            // 设置旧配置
            webClient.get()
                    // 设置URL
                    .uri("http://localhost:8888/config/update2Old").retrieve().bodyToMono(String.class).block();
        }
    }

}
