package com.jstarcraft.cloud.monitor.link;

/**
 * 追踪获取器
 * 
 * <pre>
 * 
 * </pre>
 * 
 * @author Birdy
 *
 */
public interface LinkContextReader {

    /**
     * 从媒介读出上下文
     * 
     * @return
     */
    LinkContext readContext();

}
