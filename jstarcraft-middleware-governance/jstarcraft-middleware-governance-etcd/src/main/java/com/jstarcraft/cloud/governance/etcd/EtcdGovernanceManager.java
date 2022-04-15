package com.jstarcraft.cloud.governance.etcd;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jstarcraft.cloud.governance.DefaultGovernanceInstance;
import com.jstarcraft.cloud.governance.GovernanceInstance;
import com.jstarcraft.cloud.governance.GovernanceManager;
import com.jstarcraft.core.common.conversion.json.JsonUtility;
import com.jstarcraft.core.utility.StringUtility;

import io.etcd.jetcd.ByteSequence;
import io.etcd.jetcd.Client;
import io.etcd.jetcd.KeyValue;
import io.etcd.jetcd.options.GetOption;

/**
 * Etcd治理管理器
 * 
 * @author Birdy
 *
 */
public class EtcdGovernanceManager implements GovernanceManager {

    private static final Logger logger = LoggerFactory.getLogger(EtcdGovernanceManager.class);

    private Client etcd;

    private String path;

    public EtcdGovernanceManager(Client etcd, String path) {
        this.etcd = etcd;
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
            String path = this.path + StringUtility.DOT + category + StringUtility.DOT + id;
            ByteSequence key = ByteSequence.from(path.getBytes(StringUtility.CHARSET));
            String json = JsonUtility.object2String(information);
            ByteSequence value = ByteSequence.from(json.getBytes(StringUtility.CHARSET));
            etcd.getKVClient().put(key, value).get();
        } catch (Exception exception) {
            throw new RuntimeException(exception);
        }
    }

    @Override
    public void deregisterInstance(GovernanceInstance instance) {
        try {
            String id = instance.getId();
            String category = instance.getCategory();
            String path = this.path + StringUtility.DOT + category + StringUtility.DOT + id;
            ByteSequence key = ByteSequence.from(path.getBytes(StringUtility.CHARSET));
            etcd.getKVClient().delete(key);
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
            String path = this.path;
            GetOption option = GetOption.newBuilder().withPrefix(ByteSequence.from(path.getBytes(StringUtility.CHARSET))).build();
            ByteSequence key = ByteSequence.from(path.getBytes(StringUtility.CHARSET));
            List<KeyValue> keyValues = etcd.getKVClient().get(key, option).get().getKvs();
            Set<String> categories = new LinkedHashSet<>();
            for (KeyValue keyValue : keyValues) {
                String json = keyValue.getValue().toString(StringUtility.CHARSET);
                DefaultGovernanceInstance information = JsonUtility.string2Object(json, DefaultGovernanceInstance.class);
                categories.add(information.getCategory());
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
            GetOption option = GetOption.newBuilder().withPrefix(ByteSequence.from(path.getBytes(StringUtility.CHARSET))).build();
            ByteSequence key = ByteSequence.from(path.getBytes(StringUtility.CHARSET));
            List<KeyValue> keyValues = etcd.getKVClient().get(key, option).get().getKvs();
            List<GovernanceInstance> instances = new ArrayList<>(keyValues.size());
            for (KeyValue keyValue : keyValues) {
                String json = keyValue.getValue().toString(StringUtility.CHARSET);
                DefaultGovernanceInstance information = JsonUtility.string2Object(json, DefaultGovernanceInstance.class);
                instances.add(information);
            }
            return instances;
        } catch (Exception exception) {
            throw new RuntimeException(exception);
        }
    }

}
