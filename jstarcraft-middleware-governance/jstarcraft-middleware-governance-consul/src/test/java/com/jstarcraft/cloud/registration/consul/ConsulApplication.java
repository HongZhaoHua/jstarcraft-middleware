package com.jstarcraft.cloud.registration.consul;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.ribbon.RibbonClient;

import com.jstarcraft.cloud.registration.configurer.ConsulLoadBalanceConfigurer;

@SpringBootApplication
@EnableDiscoveryClient
@RibbonClient(name = "demo", configuration = ConsulLoadBalanceConfigurer.class)
public class ConsulApplication {

    public static void main(String[] arguments) {
        SpringApplication.run(ConsulApplication.class, arguments);
    }

}
