package com.jstarcraft.cloud.profile.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

import com.jstarcraft.cloud.profile.ProfileManager;
import com.jstarcraft.cloud.profile.ProfileMonitor;
import com.jstarcraft.core.common.configuration.Configurator;
import com.jstarcraft.core.common.configuration.string.JsonConfigurator;
import com.jstarcraft.core.common.configuration.string.PropertyConfigurator;
import com.jstarcraft.core.common.configuration.string.YamlConfigurator;

/**
 * Config配置管理器
 * 
 * @author Birdy
 *
 */
public class ConfigProfileManager implements ProfileManager {

    private static final Logger logger = LoggerFactory.getLogger(ConfigProfileManager.class);

    private RestTemplate config;

    private String format;

    private String address;

    private Object[] arguments;

    public ConfigProfileManager(RestTemplate config, String format, String address, String profile, String label) {
        this.config = config;
        this.format = format;
        this.address = address + "/{label}/{name}-{profile}.{format}";
        this.arguments = new String[] { label, null, profile, format };
    }

    @Override
    public Configurator getConfiguration(String name) {
        arguments[1] = name;
        ResponseEntity<String> response = null;
        try {
            HttpHeaders headers = new HttpHeaders();
//            headers.setAccept(Collections.singletonList(MediaType.parseMediaType(V2_JSON)));
            HttpEntity<Void> request = new HttpEntity<>((Void) null, headers);
            response = config.exchange(address, HttpMethod.GET, request, String.class, arguments);
        } catch (HttpClientErrorException exception) {
            if (exception.getStatusCode() != HttpStatus.NOT_FOUND) {
                throw exception;
            }
        } catch (ResourceAccessException exception) {
            throw exception;
        }
        if (response == null || response.getStatusCode() != HttpStatus.OK) {
            return null;
        }
        String content = response.getBody();
        switch (format) {
        case "json":
            return new JsonConfigurator(content);
        case "properties":
            return new PropertyConfigurator(content);
        case "yaml":
            return new YamlConfigurator(content);
        }
        throw new IllegalArgumentException();
    }

    @Override
    public void registerMonitor(String name, ProfileMonitor monitor) {
        // TODO Auto-generated method stub

    }

    @Override
    public void unregisterMonitor(String name, ProfileMonitor monitor) {
        // TODO Auto-generated method stub

    }

}
