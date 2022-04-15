package com.jstarcraft.cloud.governance.consul;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import com.ecwid.consul.v1.ConsistencyMode;
import com.ecwid.consul.v1.ConsulClient;
import com.ecwid.consul.v1.QueryParams;
import com.ecwid.consul.v1.Response;
import com.ecwid.consul.v1.agent.model.NewService;
import com.ecwid.consul.v1.catalog.CatalogServicesRequest;
import com.ecwid.consul.v1.health.HealthServicesRequest;
import com.ecwid.consul.v1.health.model.HealthService;
import com.jstarcraft.cloud.governance.DefaultGovernanceInstance;
import com.jstarcraft.cloud.governance.GovernanceInstance;
import com.jstarcraft.cloud.governance.GovernanceManager;

/**
 * Consul治理管理器
 * 
 * @author Birdy
 *
 */
public class ConsulGovernanceManager implements GovernanceManager {

    private static final Logger logger = LoggerFactory.getLogger(ConsulGovernanceManager.class);

    private ConsulClient consul;

    public ConsulGovernanceManager(ConsulClient consul) {
        this.consul = consul;
    }

    @Override
    public void registerInstance(GovernanceInstance instance) {
        NewService service = new NewService();
        service.setId(instance.getId());
        service.setName(instance.getCategory());
        service.setAddress(instance.getHost());
        service.setPort(instance.getPort());
        service.setMeta(instance.getMetadata());
        this.consul.agentServiceRegister(service);
    }

    @Override
    public void deregisterInstance(GovernanceInstance instance) {
        this.consul.agentServiceDeregister(instance.getId());
    }

    @Override
    public void leaseInstance(GovernanceInstance instance) {
        // TODO Auto-generated method stub

    }

    private static String getHost(HealthService health) {
        HealthService.Service service = health.getService();
        HealthService.Node node = health.getNode();
        if (StringUtils.hasText(service.getAddress())) {
            return service.getAddress();
        } else if (StringUtils.hasText(node.getAddress())) {
            return node.getAddress();
        }
        return node.getNode();
    }

    @Override
    public Set<String> discoverCategories() {
        CatalogServicesRequest request = CatalogServicesRequest.newBuilder().setQueryParams(QueryParams.DEFAULT).build();
        Response<Map<String, List<String>>> response = this.consul.getCatalogServices(request);
        return response.getValue().keySet();
    }

    @Override
    public List<GovernanceInstance> discoverInstances(String category) {
        ConsistencyMode mode = ConsistencyMode.DEFAULT;
        QueryParams query = new QueryParams(mode);
        HealthServicesRequest request = HealthServicesRequest.newBuilder().setQueryParams(query).build();
        Response<List<HealthService>> response = this.consul.getHealthServices(category, request);
        List<HealthService> services = response.getValue();
        List<GovernanceInstance> instances = new ArrayList<>(services.size());
        for (HealthService service : services) {
            String id = service.getService().getId();
            String host = getHost(service);
            int port = service.getService().getPort();
            Map<String, String> metadata = service.getService().getMeta();
            GovernanceInstance instance = new DefaultGovernanceInstance(id, category, host, port, metadata);
            instances.add(instance);
        }
        return instances;
    }

}
