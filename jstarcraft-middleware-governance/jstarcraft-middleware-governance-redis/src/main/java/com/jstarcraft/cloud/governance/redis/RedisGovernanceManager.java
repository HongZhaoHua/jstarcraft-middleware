package com.jstarcraft.cloud.governance.redis;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.redisson.Redisson;
import org.redisson.api.RMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jstarcraft.cloud.governance.DefaultGovernanceInstance;
import com.jstarcraft.cloud.governance.GovernanceInstance;
import com.jstarcraft.cloud.governance.GovernanceManager;
import com.jstarcraft.core.utility.StringUtility;

/**
 * Redis治理管理器
 * 
 * @author Birdy
 *
 */
public class RedisGovernanceManager implements GovernanceManager {

    private static final Logger logger = LoggerFactory.getLogger(RedisGovernanceManager.class);

    private Redisson redis;

    private String path;

    public RedisGovernanceManager(Redisson redis, String path) {
        this.redis = redis;
        this.path = path;
    }

    @Override
    public void registerInstance(GovernanceInstance instance) {
        try {
            String id = instance.getId();
            String category = instance.getCategory();
            String host = instance.getHost();
            int port = instance.getPort();
            Map<String, String> metadata = instance.getMetadata();
            DefaultGovernanceInstance information = new DefaultGovernanceInstance(id, category, host, port, metadata);
            String path = this.path + StringUtility.DOT + category;
            RMap<String, DefaultGovernanceInstance> keyValues = this.redis.getMap(path);
            keyValues.put(id, information);
        } catch (Exception exception) {
            throw new RuntimeException(exception);
        }
    }

    @Override
    public void deregisterInstance(GovernanceInstance instance) {
        try {
            String id = instance.getId();
            String category = instance.getCategory();
            String path = this.path + StringUtility.DOT + category;
            RMap<String, DefaultGovernanceInstance> keyValues = this.redis.getMap(path);
            keyValues.remove(id);
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
            String path = this.path + StringUtility.ASTERISK;
            Set<String> categories = new LinkedHashSet<>();
            for (String category : this.redis.getKeys().getKeysByPattern(path, Integer.MAX_VALUE)) {
                categories.add(category);
            }
            return categories;
        } catch (Exception exception) {
            throw new RuntimeException(exception);
        }
    }

    @Override
    public List<GovernanceInstance> discoverInstances(String category) {
        try {
            String path = this.path + StringUtility.DOT + category;
            RMap<String, DefaultGovernanceInstance> keyValues = this.redis.getMap(path);
            List<GovernanceInstance> instances = new ArrayList<>(keyValues.size());
            for (DefaultGovernanceInstance information : keyValues.values()) {
                instances.add(information);
            }
            return instances;
        } catch (Exception exception) {
            throw new RuntimeException(exception);
        }
    }

}
