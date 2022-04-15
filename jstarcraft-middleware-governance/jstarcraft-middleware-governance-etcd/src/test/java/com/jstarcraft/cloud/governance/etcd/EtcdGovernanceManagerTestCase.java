package com.jstarcraft.cloud.governance.etcd;

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

import io.etcd.jetcd.Client;

public class EtcdGovernanceManagerTestCase {

    private static Client etcd;

    @BeforeClass
    public static void start() throws Exception {
        etcd = Client.builder().endpoints("http://127.0.0.1:2379").build();
    }

    @AfterClass
    public static void stop() throws Exception {
    }

    protected GovernanceManager getManager() {
        String path = "path";
        EtcdGovernanceManager manager = new EtcdGovernanceManager(etcd, path);
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
            port++;
        }
        while (manager.discoverInstances(name).size() == 0) {
            Thread.sleep(1000L);
        }
        Assert.assertEquals(1, manager.discoverCategories().size());
        Assert.assertEquals(3, manager.discoverInstances(name).size());

        for (GovernanceInstance instance : instances) {
            manager.deregisterInstance(instance);
        }
        while (manager.discoverInstances(name).size() != 0) {
            Thread.sleep(1000L);
        }
        Assert.assertEquals(0, manager.discoverCategories().size());
        Assert.assertEquals(0, manager.discoverInstances(name).size());
    }

}
