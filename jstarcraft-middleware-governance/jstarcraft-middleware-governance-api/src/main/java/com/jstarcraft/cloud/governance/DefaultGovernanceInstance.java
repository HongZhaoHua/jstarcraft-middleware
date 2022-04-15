package com.jstarcraft.cloud.governance;

import java.util.Map;
import java.util.Objects;

/**
 * 默认治理实例
 * 
 * @author Birdy
 *
 */
public class DefaultGovernanceInstance implements GovernanceInstance {

    /** 实例标识 */
    private String id;

    /** 实例类别 */
    private String category;

    /** 实例域名 */
    private String host;

    /** 实例端口 */
    private int port;

    /** 实例元信息 */
    private Map<String, String> metadata;

    DefaultGovernanceInstance() {
    }

    public DefaultGovernanceInstance(String id, String category, String host, int port, Map<String, String> metadata) {
        this.id = id;
        this.category = category;
        this.host = host;
        this.port = port;
        this.metadata = metadata;
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public String getCategory() {
        return category;
    }

    @Override
    public String getHost() {
        return host;
    }

    @Override
    public int getPort() {
        return port;
    }

    @Override
    public Map<String, String> getMetadata() {
        return metadata;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, category, host, port);
    }

    @Override
    public boolean equals(Object object) {
        if (this == object)
            return true;
        if (object == null)
            return false;
        if (getClass() != object.getClass())
            return false;
        DefaultGovernanceInstance that = (DefaultGovernanceInstance) object;
        return Objects.equals(id, that.id) && Objects.equals(category, that.category) && Objects.equals(host, that.host) && port == that.port;
    }

    @Override
    public String toString() {
        return "DefaultGovernanceInstance [id=" + id + ", name=" + category + ", host=" + host + ", port=" + port + ", metadata=" + metadata + "]";
    }

}
