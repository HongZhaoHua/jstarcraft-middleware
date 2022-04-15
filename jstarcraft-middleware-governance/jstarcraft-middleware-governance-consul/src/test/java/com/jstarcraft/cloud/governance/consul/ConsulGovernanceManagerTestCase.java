package com.jstarcraft.cloud.governance.consul;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import com.ecwid.consul.v1.ConsulClient;
import com.ecwid.consul.v1.ConsulRawClient;
import com.jstarcraft.cloud.governance.DefaultGovernanceInstance;
import com.jstarcraft.cloud.governance.GovernanceInstance;
import com.jstarcraft.cloud.governance.GovernanceManager;

public class ConsulGovernanceManagerTestCase {

    private static ConsulClient consul;

    @BeforeClass
    public static void start() throws Exception {
        ConsulRawClient client = new ConsulRawClient("localhost", 8500);
        consul = new ConsulClient(client);
    }

    @AfterClass
    public static void stop() throws Exception {
    }

    protected GovernanceManager getManager() {
        ConsulGovernanceManager manager = new ConsulGovernanceManager(consul);
        return manager;
    }

    @Test
    public void testDiscover() throws Exception {
        GovernanceManager manager = getManager();

        String[] ids = { "protoss", "terran", "zerg" };
        String name = "jstarcraft";
        String host = "localhost";
        int port = 1000;
        Map<String, String> metadata = new HashMap<>();
        for (String id : ids) {
            GovernanceInstance instance = new DefaultGovernanceInstance(id, name, host, port, metadata);
            manager.registerInstance(instance);
        }
        List<GovernanceInstance> instances = manager.discoverInstances(name);
        Assert.assertEquals(3, instances.size());

        for (GovernanceInstance instance : instances) {
            manager.deregisterInstance(instance);
        }
        instances = manager.discoverInstances(name);
        Assert.assertEquals(0, instances.size());
    }

}
