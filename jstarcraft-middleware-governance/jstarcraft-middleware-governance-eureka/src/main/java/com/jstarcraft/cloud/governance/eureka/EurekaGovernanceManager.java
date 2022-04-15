package com.jstarcraft.cloud.governance.eureka;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jstarcraft.cloud.governance.DefaultGovernanceInstance;
import com.jstarcraft.cloud.governance.GovernanceInstance;
import com.jstarcraft.cloud.governance.GovernanceManager;
import com.netflix.appinfo.DataCenterInfo.Name;
import com.netflix.appinfo.InstanceInfo;
import com.netflix.appinfo.MyDataCenterInfo;
import com.netflix.discovery.shared.Application;
import com.netflix.discovery.shared.Applications;
import com.netflix.discovery.shared.transport.EurekaHttpClient;

/**
 * Eureka治理管理器
 * 
 * @author Birdy
 *
 */
// TODO 考虑如何保持租约
public class EurekaGovernanceManager implements GovernanceManager {

    private static final Logger logger = LoggerFactory.getLogger(EurekaGovernanceManager.class);

    private EurekaHttpClient eureka;

    public EurekaGovernanceManager(EurekaHttpClient eureka) {
        this.eureka = eureka;
    }

    @Override
    public void registerInstance(GovernanceInstance instance) {
        InstanceInfo.Builder builder = InstanceInfo.Builder.newBuilder();
        builder.setInstanceId(instance.getId());
        builder.setAppName(instance.getCategory());
        // 必须设置IP
        builder.setIPAddr(instance.getHost());
        builder.setHostName(instance.getHost());
        builder.setPort(instance.getPort());
        builder.setMetadata(instance.getMetadata());
        // 必须设置DataCenter
        builder.setDataCenterInfo(new MyDataCenterInfo(Name.MyOwn));
        InstanceInfo information = builder.build();
        int status = eureka.register(information).getStatusCode();
    }

    @Override
    public void deregisterInstance(GovernanceInstance instance) {
        int status = eureka.cancel(instance.getCategory(), instance.getId()).getStatusCode();
    }

    @Override
    public void leaseInstance(GovernanceInstance instance) {
        // TODO Auto-generated method stub

    }

    @Override
    public Set<String> discoverCategories() {
        Applications applications = eureka.getApplications().getEntity();
        if (applications == null) {
            return Collections.emptySet();
        }
        Set<String> categories = new LinkedHashSet<>();
        for (Application application : applications.getRegisteredApplications()) {
            if (application.getInstances().isEmpty()) {
                continue;
            }
            categories.add(application.getName());
        }
        return categories;
    }

    @Override
    public List<GovernanceInstance> discoverInstances(String category) {
        Application application = eureka.getApplication(category).getEntity();
        if (application == null) {
            return Collections.emptyList();
        }
        List<InstanceInfo> informations = application.getInstances();
        List<GovernanceInstance> instances = new ArrayList<>(informations.size());
        for (InstanceInfo information : informations) {
            String id = information.getInstanceId();
            String host = information.getHostName();
            int port = information.getPort();
            Map<String, String> metadata = information.getMetadata();
            GovernanceInstance instance = new DefaultGovernanceInstance(id, category, host, port, metadata);
            instances.add(instance);
        }
        return instances;
    }

}
