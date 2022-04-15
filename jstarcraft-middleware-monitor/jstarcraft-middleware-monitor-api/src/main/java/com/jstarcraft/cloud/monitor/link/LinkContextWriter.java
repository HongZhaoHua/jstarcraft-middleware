package com.jstarcraft.cloud.monitor.link;

/**
 * 追踪设置器
 * 
 * @author Birdy
 *
 */
public interface LinkContextWriter {

    /**
     * 上下文写入到媒介
     * 
     * @param context
     */
    void writeContext(LinkContext context);

}
