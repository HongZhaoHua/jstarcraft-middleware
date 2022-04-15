package com.jstarcraft.cloud.monitor.link;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * 追踪上下文
 * 
 * <pre>
 * 用于传递
 * </pre>
 * 
 * @author Birdy
 *
 */
public class LinkContext {

    /** 根标识 */
    private String root;

    /** 单元标识 */
    private String id;

    /** 上下文属性 */
    private Map<String, String> properties;

    public LinkContext(String root, String id, Map<String, String> properties) {
        this.root = root;
        this.id = id;
        this.properties = new HashMap<>(properties);
    }

    /**
     * 获取根标识
     * 
     * @return
     */
    public String getRoot() {
        return root;
    }

    /**
     * 获取单元标识
     * 
     * @return
     */
    public String getId() {
        return id;
    }

    /**
     * 获取上下文属性
     * 
     * @return
     */
    public Map<String, String> getProperties() {
        return Collections.unmodifiableMap(properties);
    }

}
