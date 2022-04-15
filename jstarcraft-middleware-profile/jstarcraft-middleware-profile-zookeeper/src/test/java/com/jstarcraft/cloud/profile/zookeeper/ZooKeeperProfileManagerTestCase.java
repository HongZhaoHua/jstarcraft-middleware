package com.jstarcraft.cloud.profile.zookeeper;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.RetryOneTime;
import org.apache.curator.test.TestingServer;
import org.apache.zookeeper.CreateMode;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.jstarcraft.core.common.configuration.Configurator;
import com.jstarcraft.core.utility.StringUtility;

public class ZooKeeperProfileManagerTestCase {
    
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

    @Test
    public void test() throws Exception {
        String path = "/group";
        String name = "jstarcraft";
        byte[] data = "race=random".getBytes(StringUtility.CHARSET);
        curator.create().creatingParentsIfNeeded().withMode(CreateMode.EPHEMERAL).forPath(path + "/" + name, data);
        ZooKeeperProfileManager manager = new ZooKeeperProfileManager(curator, "properties", path);
        Configurator configurator = manager.getConfiguration("jstarcraft");
        Assert.assertEquals("random", configurator.getString("race"));
        curator.delete().forPath(path + "/" + name);
    }

}
