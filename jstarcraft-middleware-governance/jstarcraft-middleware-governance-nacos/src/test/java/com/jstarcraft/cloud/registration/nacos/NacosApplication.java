package com.jstarcraft.cloud.registration.nacos;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.ribbon.RibbonClient;

import com.jstarcraft.cloud.registration.configure.NacosLoadBalanceConfigurer;

@SpringBootApplication
@EnableDiscoveryClient
@RibbonClient(name = "demo", configuration = NacosLoadBalanceConfigurer.class)
public class NacosApplication {

    public static void main(String[] arguments) {
        SpringApplication.run(NacosApplication.class, arguments);
    }

}
