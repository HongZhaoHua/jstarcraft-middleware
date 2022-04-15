package com.jstarcraft.cloud.profile.consul;

import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import com.ecwid.consul.v1.ConsulClient;
import com.ecwid.consul.v1.ConsulRawClient;
import com.ecwid.consul.v1.QueryParams;
import com.jstarcraft.core.common.configuration.Configurator;

public class ConsulProfileManagerTestCase {

    private static ConsulClient consul;

    @BeforeClass
    public static void start() throws Exception {
        ConsulRawClient client = new ConsulRawClient("localhost", 8500);
        consul = new ConsulClient(client);
    }

    @AfterClass
    public static void stop() throws Exception {
    }

    @Test
    public void test() {
        consul.setKVValue("jstarcraft", "race=random", QueryParams.DEFAULT);
        ConsulProfileManager manager = new ConsulProfileManager(consul, "properties");
        Configurator configurator = manager.getConfiguration("jstarcraft");
        Assert.assertEquals("random", configurator.getString("race"));
        consul.deleteKVValue("jstarcraft", QueryParams.DEFAULT);
    }

}
