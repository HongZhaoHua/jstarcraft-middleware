package com.jstarcraft.cloud.config;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.config.server.EnableConfigServer;
import org.springframework.context.annotation.PropertySource;

@EnableConfigServer
@PropertySource(value = { "classpath:application.properties" })
@SpringBootApplication
public class ConfigMiddleware {

    public static void main(String[] arguments) {
        SpringApplication.run(ConfigMiddleware.class, arguments);
    }

}
