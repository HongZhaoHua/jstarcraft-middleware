package com.jstarcraft.cloud.profile.redis;

import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.redisson.Redisson;
import org.redisson.api.RBucket;
import org.redisson.api.RKeys;
import org.redisson.client.codec.Codec;
import org.redisson.client.codec.StringCodec;
import org.redisson.config.Config;

import com.jstarcraft.core.common.configuration.Configurator;

import redis.embedded.RedisServer;

public class RedisProfileManagerTestCase {

    private static Redisson client;

    private static RKeys keys;

    private static RedisServer server;

    @BeforeClass
    public static void start() throws Exception {
        server = RedisServer.builder().port(6379).setting("maxmemory 64M").build();
        server.start();
        // 注意此处的编解码器
        Codec codec = new StringCodec();
        Config configuration = new Config();
        configuration.setCodec(codec);
        configuration.useSingleServer().setAddress("redis://127.0.0.1:6379");

        client = (Redisson) Redisson.create(configuration);
        keys = client.getKeys();
        keys.flushdb();
    }

    @AfterClass
    public static void stop() throws Exception {
        keys.flushdb();
        client.shutdown();
        server.stop();
    }

    @Test
    public void test() {
        String name = "jstarcraft";
        RBucket<String> bucket = client.getBucket(name);
        bucket.set("race=random");
        RedisProfileManager manager = new RedisProfileManager(client, "properties");
        Configurator configurator = manager.getConfiguration("jstarcraft");
        Assert.assertEquals("random", configurator.getString("race"));
        bucket.delete();
    }

}
