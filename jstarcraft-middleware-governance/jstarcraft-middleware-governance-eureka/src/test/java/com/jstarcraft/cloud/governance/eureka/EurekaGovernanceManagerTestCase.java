package com.jstarcraft.cloud.governance.eureka;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import com.jstarcraft.cloud.governance.DefaultGovernanceInstance;
import com.jstarcraft.cloud.governance.GovernanceInstance;
import com.jstarcraft.cloud.governance.GovernanceManager;
import com.netflix.discovery.shared.resolver.DefaultEndpoint;
import com.netflix.discovery.shared.resolver.EurekaEndpoint;
import com.netflix.discovery.shared.transport.EurekaHttpClient;
import com.netflix.discovery.shared.transport.TransportClientFactory;
import com.netflix.discovery.shared.transport.jersey.JerseyEurekaHttpClientFactory;

public class EurekaGovernanceManagerTestCase {

    private static EurekaHttpClient eureka;

    @BeforeClass
    public static void start() throws Exception {
        String url = "http://localhost:8761/eureka/";
        // 注意,无论使用哪种EurekaHttpClient尽量通过TransportClientFactory构建,否则需要使用CodecWrapper编解码
        EurekaEndpoint endpoint = new DefaultEndpoint(url);
        TransportClientFactory factory = JerseyEurekaHttpClientFactory.newBuilder().withClientName("Eureka").build();
        eureka = factory.newClient(endpoint);
    }

    @AfterClass
    public static void stop() throws Exception {
        eureka.shutdown();
    }

    protected GovernanceManager getManager() {
        EurekaGovernanceManager manager = new EurekaGovernanceManager(eureka);
        return manager;
    }

    @Test
    public void testDiscover() throws Exception {
        GovernanceManager manager = getManager();
        String[] ids = { "protoss", "terran", "zerg" };
        String name = "JSTARCRAFT";
        String host = "localhost";
        int port = 1000;
        Map<String, String> metadata = new HashMap<>();

        List<GovernanceInstance> instances = new ArrayList<>(ids.length);
        for (String id : ids) {
            GovernanceInstance instance = new DefaultGovernanceInstance(id, name, host, port, metadata);
            manager.registerInstance(instance);
            instances.add(instance);
        }
        while (manager.discoverInstances(name).size() == 0) {
            Thread.sleep(1000L);
        }
        Assert.assertEquals(3, manager.discoverInstances(name).size());

        for (GovernanceInstance instance : instances) {
            manager.deregisterInstance(instance);
        }
        while (manager.discoverInstances(name).size() != 0) {
            Thread.sleep(1000L);
        }
        Assert.assertEquals(0, manager.discoverInstances(name).size());
    }

}
