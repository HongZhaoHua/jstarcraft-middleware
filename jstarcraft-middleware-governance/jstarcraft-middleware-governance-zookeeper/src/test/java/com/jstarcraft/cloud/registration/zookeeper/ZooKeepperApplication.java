package com.jstarcraft.cloud.registration.zookeeper;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.ribbon.RibbonClient;

import com.jstarcraft.cloud.registration.configure.ZooKeeperLoadBalanceConfigurer;

@SpringBootApplication
@EnableDiscoveryClient
@RibbonClient(name = "demo", configuration = ZooKeeperLoadBalanceConfigurer.class)
public class ZooKeepperApplication {

    public static void main(String[] arguments) {
        SpringApplication.run(ZooKeepperApplication.class, arguments);
    }

}
