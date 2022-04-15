package com.jstarcraft.cloud.governance.zookeeper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.RetryOneTime;
import org.apache.curator.test.TestingServer;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.jstarcraft.cloud.governance.DefaultGovernanceInstance;
import com.jstarcraft.cloud.governance.GovernanceInstance;
import com.jstarcraft.cloud.governance.GovernanceManager;

public class ZooKeeperGovernanceManagerTestCase {

    private TestingServer zookeeper;

    private CuratorFramework curator;

    @Before
    public void testBefore() throws Exception {
        zookeeper = new TestingServer();
        curator = CuratorFrameworkFactory.builder()

                .namespace("ZooKeeperAtomicIdentityFactoryTestCase")

                .connectString(zookeeper.getConnectString())

                .retryPolicy(new RetryOneTime(2000))

                .build();
        curator.start();
    }

    @After
    public void testAfter() throws Exception {
        curator.close();
        zookeeper.stop();
    }

    protected GovernanceManager getManager() {
        String path = "/path";
        ZooKeeperGovernanceManager manager = new ZooKeeperGovernanceManager(curator, path);
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
