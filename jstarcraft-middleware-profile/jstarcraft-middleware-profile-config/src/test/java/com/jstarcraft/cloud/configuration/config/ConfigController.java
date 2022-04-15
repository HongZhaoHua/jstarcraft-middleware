package com.jstarcraft.cloud.configuration.config;

import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.refresh.ContextRefresher;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("config")
public class ConfigController {

    @Autowired
    private ConfigProperties properties;

    @Autowired
    private ContextRefresher refresher;

    @GetMapping("/get")
    public String getConfig() {
        return properties.getConfig();
    }

    @GetMapping("/refresh")
    public Set<String> refreshConfig() {
        return refresher.refresh();
    }

}
