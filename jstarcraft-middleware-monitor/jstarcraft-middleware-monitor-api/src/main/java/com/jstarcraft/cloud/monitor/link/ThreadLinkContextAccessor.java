package com.jstarcraft.cloud.monitor.link;

/**
 * 基于线程的追踪访问器
 * 
 * <pre>
 * 通过组合各种ThreadLocal达到各种效果
 * </pre>
 * 
 * @author Birdy
 *
 */
public class ThreadLinkContextAccessor implements LinkContextReader, LinkContextWriter {

    private final ThreadLocal<LinkContext> contexts;

    private ThreadLinkContextAccessor(ThreadLocal<LinkContext> contexts) {
        this.contexts = contexts;
    }

    @Override
    public LinkContext readContext() {
        return contexts.get();
    }

    @Override
    public void writeContext(LinkContext context) {
        contexts.set(context);
    }

}
