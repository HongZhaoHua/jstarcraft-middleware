package com.jstarcraft.cloud.governance.nacos;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.nacos.api.naming.NamingService;
import com.alibaba.nacos.api.naming.pojo.Instance;
import com.jstarcraft.cloud.governance.DefaultGovernanceInstance;
import com.jstarcraft.cloud.governance.GovernanceInstance;
import com.jstarcraft.cloud.governance.GovernanceManager;

/**
 * Nacos治理管理器
 * 
 * @author Birdy
 *
 */
// 注意:Nacos必须保证IP+端口唯一
public class NacosGovernanceManager implements GovernanceManager {

    private static final Logger logger = LoggerFactory.getLogger(NacosGovernanceManager.class);

    private NamingService nacos;

    public NacosGovernanceManager(NamingService nacos) {
        this.nacos = nacos;
    }

    @Override
    public void registerInstance(GovernanceInstance instance) {
        try {
            Instance information = new Instance();
            information.setInstanceId(instance.getId());
            // 设置IP
            information.setIp(instance.getHost());
            // 设置端口
            information.setPort(instance.getPort());
            // 设置类别
            information.setServiceName(instance.getCategory());
            // TODO 设置上下线(true:上线 false:下线)
            information.setEnabled(true);
            // 设置健康
            information.setHealthy(true);
            // 设置权重
            information.setWeight(1D);
            information.setMetadata(instance.getMetadata());// 元数据
            nacos.registerInstance(instance.getCategory(), information);
        } catch (Exception exception) {
            throw new RuntimeException(exception);
        }
    }

    @Override
    public void deregisterInstance(GovernanceInstance instance) {
        try {
            Instance information = new Instance();
            information.setInstanceId(instance.getId());
            // 设置IP
            information.setIp(instance.getHost());
            // 设置端口
            information.setPort(instance.getPort());
            // 设置类别
            information.setServiceName(instance.getCategory());
            // TODO 设置上下线(true:上线 false:下线)
            information.setEnabled(true);
            // 设置健康
            information.setHealthy(true);
            // 设置权重
            information.setWeight(1D);
            information.setMetadata(instance.getMetadata());// 元数据
            nacos.deregisterInstance(instance.getCategory(), information);
        } catch (Exception exception) {
            throw new RuntimeException(exception);
        }
    }

    @Override
    public void leaseInstance(GovernanceInstance instance) {
        // TODO Auto-generated method stub

    }

    @Override
    public Set<String> discoverCategories() {
        try {
            return new LinkedHashSet<>(nacos.getServicesOfServer(1, Integer.MAX_VALUE).getData());
        } catch (Exception exception) {
            throw new RuntimeException(exception);
        }
    }

    @Override
    public List<GovernanceInstance> discoverInstances(String category) {
        try {
            List<Instance> informations = nacos.selectInstances(category, true);
            List<GovernanceInstance> instances = new ArrayList<>(informations.size());
            for (Instance information : informations) {
                String id = information.getInstanceId();
                String host = information.getIp();
                int port = information.getPort();
                Map<String, String> metadata = information.getMetadata();
                GovernanceInstance instance = new DefaultGovernanceInstance(id, category, host, port, metadata);
                instances.add(instance);
            }
            return instances;
        } catch (Exception exception) {
            throw new RuntimeException(exception);
        }
    }

}
