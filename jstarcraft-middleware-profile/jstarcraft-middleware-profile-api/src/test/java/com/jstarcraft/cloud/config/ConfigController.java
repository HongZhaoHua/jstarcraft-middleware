package com.jstarcraft.cloud.config;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("config")
public class ConfigController {

    @GetMapping("/update2New")
    public String update2New() throws Exception {
        File configFile = new File(this.getClass().getClassLoader().getResource("config/config-test.properties").toURI());
        File newFile = new File(this.getClass().getClassLoader().getResource("config/config-test.new").toURI());
        File oldFile = new File(this.getClass().getClassLoader().getResource("config/config-test.old").toURI());
        FileUtils.copyFile(newFile, configFile);
        return "new";
    }

    @GetMapping("/update2Old")
    public String update2Old() throws Exception {
        File configFile = new File(this.getClass().getClassLoader().getResource("config/config-test.properties").toURI());
        File newFile = new File(this.getClass().getClassLoader().getResource("config/config-test.new").toURI());
        File oldFile = new File(this.getClass().getClassLoader().getResource("config/config-test.old").toURI());
        FileUtils.copyFile(oldFile, configFile);
        return "old";
    }

}
