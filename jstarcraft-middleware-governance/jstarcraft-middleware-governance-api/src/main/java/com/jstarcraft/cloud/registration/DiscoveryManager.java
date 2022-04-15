package com.jstarcraft.cloud.registration;

import java.util.Iterator;
import java.util.List;
import java.util.function.Predicate;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;

/**
 * 发现管理器
 * 
 * <pre>
 * 包装器模式
 * 支持自定义筛选{@link ServiceInstance}
 * </pre>
 * 
 * @author Birdy
 *
 */
public class DiscoveryManager implements DiscoveryClient {

    private static final Logger logger = LoggerFactory.getLogger(DiscoveryManager.class);

    private DiscoveryClient springDiscovery;

    /** 实例筛选器 */
    private Predicate<ServiceInstance> instancePredicate;

    /** 服务筛选器 */
    private Predicate<String> servicePredicate;

    public DiscoveryManager(DiscoveryClient springDiscovery, Predicate<ServiceInstance> instancePredicate, Predicate<String> servicePredicate) {
        this.springDiscovery = springDiscovery;
        this.instancePredicate = instancePredicate;
        this.servicePredicate = servicePredicate;
    }

    @Override
    public List<ServiceInstance> getInstances(String serviceId) {
        List<ServiceInstance> instances = springDiscovery.getInstances(serviceId);
        Iterator<ServiceInstance> iterator = instances.iterator();
        while (iterator.hasNext()) {
            if (!instancePredicate.test(iterator.next())) {
                iterator.remove();
            }
        }
        return instances;
    }

    @Override
    public List<String> getServices() {
        List<String> services = springDiscovery.getServices();
        Iterator<String> iterator = services.iterator();
        while (iterator.hasNext()) {
            if (!servicePredicate.test(iterator.next())) {
                iterator.remove();
            }
        }
        return services;
    }

    @Override
    public String description() {
        return springDiscovery.description();
    }

}