package com.jstarcraft.cloud.registration.redis;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.client.discovery.composite.CompositeDiscoveryClientAutoConfiguration;
import org.springframework.cloud.client.discovery.simple.SimpleDiscoveryClientAutoConfiguration;
import org.springframework.cloud.netflix.ribbon.RibbonClient;
import org.springframework.context.ConfigurableApplicationContext;

import com.jstarcraft.cloud.registration.configurer.RedisLoadBalanceConfigurer;

@EnableDiscoveryClient
@SpringBootApplication(exclude = { SimpleDiscoveryClientAutoConfiguration.class, CompositeDiscoveryClientAutoConfiguration.class })
@RibbonClient(name = "demo", configuration = RedisLoadBalanceConfigurer.class)
public class MockRedisApplication {

    public static void main(String[] args) {
        ConfigurableApplicationContext applicationContext = SpringApplication.run(MockRedisApplication.class, args);
        DiscoveryClient discoveryClient = applicationContext.getBean(DiscoveryClient.class);
        discoveryClient.getServices().forEach(action -> {
            System.out.println(action);
        });
    }

}
