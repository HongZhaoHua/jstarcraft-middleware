package com.jstarcraft.cloud.monitor.link;

import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import io.opentracing.Span;
import io.opentracing.mock.MockSpan;
import io.opentracing.mock.MockTracer;

public class OpenTraceTestCase {

    @Test
    public void test() throws Exception {
        // 获取追踪器
        try (MockTracer tracer = new MockTracer()) {
            Span span = tracer.buildSpan("root").start();
            span.setBaggageItem("key", "value");
            span.context().toTraceId();
            span.context().toSpanId();
            span.finish();
            List<MockSpan> spans = tracer.finishedSpans();
            Assert.assertEquals(1, spans.size());
        }
    }

}
