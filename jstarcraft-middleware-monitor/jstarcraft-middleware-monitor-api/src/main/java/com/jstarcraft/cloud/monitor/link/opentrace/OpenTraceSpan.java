package com.jstarcraft.cloud.monitor.link.opentrace;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import com.jstarcraft.cloud.monitor.link.LinkContext;
import com.jstarcraft.cloud.monitor.link.LinkSpan;
import com.jstarcraft.core.common.lifecycle.LifecycleState;

import io.opentracing.Span;

public class OpenTraceSpan implements LinkSpan, AutoCloseable {

    private String name;

    private String parent;

    private Instant begin;

    private Instant end;

    private LifecycleState state;

    private Span span;

    @Override
    public String getRoot() {
        return span.context().toTraceId();
    }

    @Override
    public String getParent() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String getId() {
        return span.context().toSpanId();
    }

    @Override
    public String getName() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Instant getBegin() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Instant getEnd() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public LifecycleState getState() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public LinkContext getContext() {
        String root = getRoot(), id = getId();
        Map<String, String> properties = new HashMap<>();
        for (Entry<String, String> term : span.context().baggageItems()) {
            properties.put(term.getKey(), term.getValue());
        }
        LinkContext context = new LinkContext(root, id, properties);
        return context;
    }

    @Override
    public void close() throws Exception {
        // TODO Auto-generated method stub
        
    }

}
