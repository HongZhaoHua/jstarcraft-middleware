package com.jstarcraft.cloud.eureka;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;
import org.springframework.context.annotation.PropertySource;

@EnableEurekaServer
@PropertySource(value = { "classpath:application.yml" })
@SpringBootApplication
public class EurekaMiddleware {

    public static void main(String[] arguments) {
        SpringApplication.run(EurekaMiddleware.class, arguments);
    }

}
