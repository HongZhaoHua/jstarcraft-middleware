package com.jstarcraft.cloud.monitor.link.opencensus;

import java.util.LinkedList;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.Callable;
import java.util.concurrent.Executors;

import com.jstarcraft.cloud.monitor.link.LinkContext;
import com.jstarcraft.cloud.monitor.link.LinkManager;
import com.jstarcraft.cloud.monitor.link.LinkSpan;
import com.jstarcraft.core.utility.RandomUtility;

import io.opencensus.trace.Span;
import io.opencensus.trace.SpanContext;
import io.opencensus.trace.SpanId;
import io.opencensus.trace.TraceId;
import io.opencensus.trace.TraceOptions;
import io.opencensus.trace.Tracer;
import io.opencensus.trace.Tracestate;
import io.opencensus.trace.Tracestate.Builder;

public class OpenCensusManager implements LinkManager {

    private final ThreadLocal<LinkedList<OpenCensusSpan>> spans = new ThreadLocal<LinkedList<OpenCensusSpan>>() {

        @Override
        protected LinkedList<OpenCensusSpan> initialValue() {
            return new LinkedList<>();
        }

    };

    private Tracer tracer;

    @Override
    public <V> V doSpan(String name, LinkContext context, Map<String, String> properties, Callable<V> task) {
        SpanContext parent;
        if (context == null) {
            // root == true
            TraceId root = TraceId.generateRandomId(RandomUtility.getRandom());
            SpanId id = SpanId.generateRandomId(RandomUtility.getRandom());
            TraceOptions option = TraceOptions.builder().build();
            Builder builder = Tracestate.builder();
            for (Entry<String, String> term : properties.entrySet()) {
                builder.set(term.getKey(), term.getValue());
            }
            parent = SpanContext.create(root, id, option, builder.build());
        } else {
            // root == false
            TraceId traceId = TraceId.fromLowerBase16(context.getRoot());
            SpanId spanId = SpanId.fromLowerBase16(context.getId());
            TraceOptions traceOption = TraceOptions.builder().build();
            Builder builder = Tracestate.builder();
            for (Entry<String, String> term : context.getProperties().entrySet()) {
                builder.set(term.getKey(), term.getValue());
            }
            for (Entry<String, String> term : properties.entrySet()) {
                builder.set(term.getKey(), term.getValue());
            }
            parent = SpanContext.create(traceId, spanId, traceOption, builder.build());
        }
        // TODO 注意:按照目前OpenCensus逻辑,只要想携带上下文信息,无论如何都无法构建根Span
        Span span = tracer.spanBuilderWithRemoteParent(name, parent).startSpan();
        try {
            spans.get().addLast(new OpenCensusSpan(name, context == null ? null : context.getId(), span));
            return task.call();
        } catch (Exception exception) {
            // TODO 考虑重构为追踪异常
            throw new RuntimeException(exception);
        } finally {
            spans.get().removeLast();
            span.end();
        }
    }

    @Override
    public void doSpan(String name, LinkContext context, Map<String, String> properties, Runnable task) {
        doSpan(name, context, properties, Executors.callable(task));
    }

    @Override
    public LinkSpan getSpan() {
        return spans.get().getLast();
    }

}
