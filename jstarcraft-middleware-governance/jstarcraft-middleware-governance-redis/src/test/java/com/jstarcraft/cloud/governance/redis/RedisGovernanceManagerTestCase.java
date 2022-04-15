package com.jstarcraft.cloud.governance.redis;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.redisson.Redisson;
import org.redisson.api.RKeys;
import org.redisson.client.codec.Codec;
import org.redisson.codec.JsonJacksonCodec;
import org.redisson.config.Config;

import com.jstarcraft.cloud.governance.DefaultGovernanceInstance;
import com.jstarcraft.cloud.governance.GovernanceInstance;
import com.jstarcraft.cloud.governance.GovernanceManager;

public class RedisGovernanceManagerTestCase {

    private static Redisson redis;

    private static RKeys keys;

    @BeforeClass
    public static void start() throws Exception {
        // 注意此处的编解码器
        Codec codec = new JsonJacksonCodec();
        Config configuration = new Config();
        configuration.setCodec(codec);
        configuration.useSingleServer().setAddress("redis://127.0.0.1:6379");
        redis = (Redisson) Redisson.create(configuration);
        keys = redis.getKeys();
        keys.flushdb();
    }

    @AfterClass
    public static void stop() throws Exception {
        keys.flushdb();
        redis.shutdown();
    }

    protected GovernanceManager getManager() {
        String path = "path";
        RedisGovernanceManager manager = new RedisGovernanceManager(redis, path);
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
