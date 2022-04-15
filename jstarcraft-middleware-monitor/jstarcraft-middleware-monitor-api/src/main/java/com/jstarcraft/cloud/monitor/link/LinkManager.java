package com.jstarcraft.cloud.monitor.link;

import java.util.Map;
import java.util.concurrent.Callable;

/**
 * 追踪管理器
 * 
 * @author Birdy
 *
 */
public interface LinkManager {

    /**
     * 执行追踪单元
     * 
     * @param <V>
     * @param name
     * @param context
     * @param properties
     * @param task
     * @return
     */
    <V> V doSpan(String name, LinkContext context, Map<String, String> properties, Callable<V> task);

    /**
     * 执行追踪单元
     * 
     * @param name
     * @param context
     * @param properties
     * @param task
     */
    void doSpan(String name, LinkContext context, Map<String, String> properties, Runnable task);

    /**
     * 获取追踪单元
     * 
     * @return
     */
    LinkSpan getSpan();

}
