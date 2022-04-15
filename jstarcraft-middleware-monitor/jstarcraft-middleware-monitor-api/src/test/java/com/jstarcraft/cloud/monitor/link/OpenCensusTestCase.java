package com.jstarcraft.cloud.monitor.link;

import java.util.Collection;
import java.util.concurrent.atomic.AtomicInteger;

import org.junit.Assert;
import org.junit.Test;
import org.mockito.invocation.MockHandler;

import com.jstarcraft.core.utility.RandomUtility;

import io.opencensus.trace.Span;
import io.opencensus.trace.SpanContext;
import io.opencensus.trace.SpanId;
import io.opencensus.trace.TraceId;
import io.opencensus.trace.TraceOptions;
import io.opencensus.trace.Tracer;
import io.opencensus.trace.Tracestate;
import io.opencensus.trace.Tracestate.Entry;
import io.opencensus.trace.Tracing;
import io.opencensus.trace.config.TraceConfig;
import io.opencensus.trace.config.TraceParams;
import io.opencensus.trace.export.SpanData;
import io.opencensus.trace.export.SpanExporter;
import io.opencensus.trace.export.SpanExporter.Handler;
import io.opencensus.trace.samplers.Samplers;

public class OpenCensusTestCase {

    @Test
    public void test() throws Exception {
        // 获取追踪配置
        TraceConfig traceConfig = Tracing.getTraceConfig();
        // 获取追踪参数
        TraceParams traceParams = traceConfig.getActiveTraceParams();
        // 设置采样率为100%
        traceParams = traceParams.toBuilder().setSampler(Samplers.alwaysSample()).build();
        // 设置最总参数
        traceConfig.updateActiveTraceParams(traceParams);
        AtomicInteger counter = new AtomicInteger();
        SpanExporter exporter = Tracing.getExportComponent().getSpanExporter();
        // 注册追踪单元处理器
        exporter.registerHandler("mock", new Handler() {

            @Override
            public void export(Collection<SpanData> datas) {
                counter.addAndGet(datas.size());
            }

        });
        // 获取追踪器
        Tracer tracer = Tracing.getTracer();

        TraceId traceId = TraceId.generateRandomId(RandomUtility.getRandom());
        SpanId spanId = SpanId.generateRandomId(RandomUtility.getRandom());
        TraceOptions traceOption = TraceOptions.builder().build();
        Tracestate traceState = Tracestate.builder().set("key", "value").build();
        SpanContext spanContext = SpanContext.create(traceId, spanId, traceOption, traceState);

        // TODO 注意:按照目前OpenCensus逻辑,只要想携带上下文信息,无论如何都无法构建根Span
        Span span = tracer.spanBuilderWithRemoteParent("root", spanContext).startSpan();
        Assert.assertEquals(traceId, span.getContext().getTraceId());
        Assert.assertNotEquals(spanId, span.getContext().getSpanId());
        for (Entry term : span.getContext().getTracestate().getEntries()) {
            Assert.assertEquals("key", term.getKey());
            Assert.assertEquals("value", term.getValue());
        }
        span.end();

        Thread.sleep(5000L);

        Assert.assertEquals(1, counter.get());
        // 注销追踪单元处理器
        exporter.unregisterHandler("mock");
    }

}
