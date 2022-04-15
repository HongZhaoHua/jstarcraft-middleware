package com.jstarcraft.cloud.configuration.config;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import org.springframework.boot.Banner.Mode;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.config.ConfigFileApplicationListener;
import org.springframework.cloud.bootstrap.BootstrapApplicationListener;
import org.springframework.cloud.context.environment.EnvironmentChangeEvent;
import org.springframework.cloud.context.refresh.ContextRefresher;
import org.springframework.cloud.context.scope.refresh.RefreshScope;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.CommandLinePropertySource;
import org.springframework.core.env.CompositePropertySource;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.EnumerablePropertySource;
import org.springframework.core.env.MapPropertySource;
import org.springframework.core.env.MutablePropertySources;
import org.springframework.core.env.PropertySource;
import org.springframework.core.env.StandardEnvironment;
import org.springframework.web.context.support.StandardServletEnvironment;

import com.jstarcraft.cloud.configuration.ConfigurationManager;
import com.jstarcraft.cloud.configuration.ConfigurationMonitor;
import com.jstarcraft.core.common.configuration.Configurator;
import com.jstarcraft.core.common.configuration.string.MapConfigurator;

/**
 * Spring Cloud Config配置管理器
 * 
 * <pre>
 * PropertySourceLocator用于引导配置
 * ContextRefresher用于刷新配置
 * </pre>
 * 
 * @author Birdy
 *
 */
public class ConfigConfigurationManager extends ContextRefresher implements ConfigurationManager {

    private static final String REFRESH_ARGS_PROPERTY_SOURCE = "refreshArgs";

    private static final String[] DEFAULT_PROPERTY_SOURCES = new String[] { CommandLinePropertySource.COMMAND_LINE_PROPERTY_SOURCE_NAME, "defaultProperties" };

    private static final HashSet<String> STANDARD_SOURCES = new HashSet<>(Arrays.asList(StandardEnvironment.SYSTEM_PROPERTIES_PROPERTY_SOURCE_NAME, StandardEnvironment.SYSTEM_ENVIRONMENT_PROPERTY_SOURCE_NAME, StandardServletEnvironment.JNDI_PROPERTY_SOURCE_NAME, StandardServletEnvironment.SERVLET_CONFIG_PROPERTY_SOURCE_NAME, StandardServletEnvironment.SERVLET_CONTEXT_PROPERTY_SOURCE_NAME, "configurationProperties"));

    private ReentrantReadWriteLock lock = new ReentrantReadWriteLock();

    private HashMap<String, Configurator> configurations = new HashMap<>();

    private HashSet<ConfigurationMonitor> monitors = new HashSet<>();

    private String getName(String path) {
        int index = path.lastIndexOf('/');
        if (index != -1) {
            path = path.substring(index + 1);
        }
        index = path.lastIndexOf('.');
        if (index != -1) {
            path = path.substring(0, index);
        }
        index = path.lastIndexOf('-');
        if (index != -1) {
            path = path.substring(0, index);
        }
        return path;
    }

    public ConfigConfigurationManager(ConfigurableApplicationContext context, RefreshScope scope) {
        super(context, scope);
        LinkedHashMap<String, HashMap<String, String>> properties = getProperties(context.getEnvironment().getPropertySources());
        for (Entry<String, HashMap<String, String>> term : properties.entrySet()) {
            String key = term.getKey();
            key = getName(key);
            HashMap<String, String> value = term.getValue();
            configurations.put(key, new MapConfigurator(value));
        }
    }

    private ConfigurableApplicationContext buildEnvironment() {
        ConfigurableApplicationContext capture = null;
        ConfigurableApplicationContext context = getContext();
        try {
            StandardEnvironment environment = copyEnvironment(context.getEnvironment());
            SpringApplicationBuilder builder = new SpringApplicationBuilder(Empty.class).bannerMode(Mode.OFF).web(WebApplicationType.NONE).environment(environment);
            builder.application().setListeners(Arrays.asList(new BootstrapApplicationListener(), new ConfigFileApplicationListener()));
            capture = builder.run();
            if (environment.getPropertySources().contains(REFRESH_ARGS_PROPERTY_SOURCE)) {
                environment.getPropertySources().remove(REFRESH_ARGS_PROPERTY_SOURCE);
            }
            MutablePropertySources sources = context.getEnvironment().getPropertySources();
            String current = null;
            for (PropertySource<?> source : environment.getPropertySources()) {
                String name = source.getName();
                if (sources.contains(name)) {
                    current = name;
                }
                if (!this.STANDARD_SOURCES.contains(name)) {
                    if (sources.contains(name)) {
                        sources.replace(name, source);
                    } else {
                        if (current != null) {
                            sources.addAfter(current, source);
                        } else {
                            sources.addFirst(source);
                            current = name;
                        }
                    }
                }
            }
        } finally {
            ConfigurableApplicationContext current = capture;
            while (current != null) {
                try {
                    current.close();
                } catch (Exception exception) {
                    // TODO Ignore;
                }
                if (current.getParent() instanceof ConfigurableApplicationContext) {
                    current = (ConfigurableApplicationContext) current.getParent();
                } else {
                    break;
                }
            }
        }
        return capture;
    }

    private StandardEnvironment copyEnvironment(ConfigurableEnvironment environment) {
        StandardEnvironment copy = new StandardEnvironment();
        MutablePropertySources sources = copy.getPropertySources();
        for (String name : DEFAULT_PROPERTY_SOURCES) {
            if (environment.getPropertySources().contains(name)) {
                if (sources.contains(name)) {
                    sources.replace(name, environment.getPropertySources().get(name));
                } else {
                    sources.addLast(environment.getPropertySources().get(name));
                }
            }
        }
        copy.setActiveProfiles(environment.getActiveProfiles());
        copy.setDefaultProfiles(environment.getDefaultProfiles());
        HashMap<String, Object> properties = new HashMap<>();
        properties.put("spring.jmx.enabled", false);
        properties.put("spring.main.sources", "");
        sources.addFirst(new MapPropertySource(REFRESH_ARGS_PROPERTY_SOURCE, properties));
        return copy;
    }

    private <T> Map<String, T> changes(Map<String, T> left, Map<String, T> right) {
        HashMap<String, T> changes = new HashMap<>();
        for (String key : left.keySet()) {
            if (!right.containsKey(key)) {
                changes.put(key, null);
            } else if (!equal(left.get(key), right.get(key))) {
                changes.put(key, right.get(key));
            }
        }
        for (String key : right.keySet()) {
            if (!left.containsKey(key)) {
                changes.put(key, right.get(key));
            }
        }
        return changes;
    }

    private boolean equal(Object left, Object right) {
        if (left == null && right == null) {
            return true;
        }
        if (left == null || right == null) {
            return false;
        }
        return left.equals(right);
    }

    private LinkedHashMap<String, HashMap<String, String>> getProperties(MutablePropertySources sources) {
        LinkedHashMap<String, HashMap<String, String>> properties = new LinkedHashMap<>();
        LinkedList<PropertySource<?>> elements = new LinkedList<>();
        for (PropertySource<?> element : sources) {
            elements.addFirst(element);
        }
        for (PropertySource<?> element : elements) {
            if (!this.STANDARD_SOURCES.contains(element.getName())) {
                getProperties(element, properties);
            }
        }
        return properties;
    }

    private void getProperties(PropertySource<?> source, LinkedHashMap<String, HashMap<String, String>> properties) {
        if (source instanceof CompositePropertySource) {
            try {
                LinkedList<PropertySource<?>> elements = new LinkedList<>();
                for (PropertySource<?> element : ((CompositePropertySource) source).getPropertySources()) {
                    elements.addFirst(element);
                }
                for (PropertySource<?> element : elements) {
                    getProperties(element, properties);
                }
            } catch (Exception exception) {
                // TODO 此处需要记录日志
                return;
            }
        } else if (source instanceof EnumerablePropertySource) {
            HashMap<String, String> keyValue = new HashMap<>();
            properties.put(source.getName(), keyValue);
            for (String key : ((EnumerablePropertySource<?>) source).getPropertyNames()) {
                keyValue.put(key, (String) source.getProperty(key));
            }
        }
    }

    @Override
    public Set<String> refreshEnvironment() {
        Lock write = lock.writeLock();
        try {
            write.lock();
            ConfigurableApplicationContext context = getContext();
            LinkedHashMap<String, HashMap<String, String>> befores = getProperties(context.getEnvironment().getPropertySources());
            buildEnvironment();
            LinkedHashMap<String, HashMap<String, String>> afters = getProperties(context.getEnvironment().getPropertySources());
            HashMap<String, Object> left = new HashMap<>();
            HashMap<String, Object> right = new HashMap<>();

            for (Entry<String, HashMap<String, String>> term : befores.entrySet()) {
                String key = term.getKey();
                key = getName(key);
                HashMap<String, String> before = term.getValue();
                HashMap<String, String> after = afters.get(key);
                if (after == null) {
                    Configurator from = new MapConfigurator(term.getValue());
                    Configurator to = null;
                    configurations.remove(key);
                    for (ConfigurationMonitor monitor : monitors) {
                        monitor.change(key, from, to);
                    }
                } else if (!equal(before, after)) {
                    Configurator from = new MapConfigurator(before);
                    Configurator to = new MapConfigurator(after);
                    configurations.put(key, to);
                    for (ConfigurationMonitor monitor : monitors) {
                        monitor.change(key, from, to);
                    }
                }
                left.putAll(before);
            }
            for (Entry<String, HashMap<String, String>> term : afters.entrySet()) {
                String key = term.getKey();
                key = getName(key);
                HashMap<String, String> before = befores.get(key);
                HashMap<String, String> after = term.getValue();
                if (before == null) {
                    Configurator from = null;
                    Configurator to = new MapConfigurator(after);
                    configurations.put(key, to);
                    for (ConfigurationMonitor monitor : monitors) {
                        monitor.change(key, from, to);
                    }
                }
                right.putAll(after);
            }

            Set<String> changes = changes(left, right).keySet();
            context.publishEvent(new EnvironmentChangeEvent(context, changes));
            return changes;
        } finally {
            write.unlock();
        }
    }

    @Override
    public Set<String> refresh() {
        Lock write = lock.writeLock();
        try {
            write.lock();
            Set<String> changes = refreshEnvironment();
            RefreshScope scope = getScope();
            scope.refreshAll();
            return changes;
        } finally {
            write.unlock();
        }
    }

    @Override
    public Configurator getConfiguration(String name) {
        Lock read = lock.readLock();
        try {
            read.lock();
            return configurations.get(name);
        } finally {
            read.unlock();
        }
    }

    @Override
    public void registerMonitor(String name, ConfigurationMonitor monitor) {
        Lock write = lock.writeLock();
        try {
            write.lock();
            monitors.add(monitor);
        } finally {
            write.unlock();
        }
    }

    @Override
    public void unregisterMonitor(String name, ConfigurationMonitor monitor) {
        Lock write = lock.writeLock();
        try {
            write.lock();
            monitors.remove(monitor);
        } finally {
            write.unlock();
        }
    }

}
