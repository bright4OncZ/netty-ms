package com.netty.netty.t1;

import com.netty.util.ByteBufferUtil;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;

public class TestByteBufferString {

    private static final Logger log = LoggerFactory.getLogger(TestByteBufferString.class);

    @Test
    public void test() {
        final ByteBuffer buffer = ByteBuffer.allocate(16);
        buffer.put("hello".getBytes());

        ByteBufferUtil.debugAll(buffer);

        ByteBuffer buffer2 = StandardCharsets.UTF_8.encode("hello");
        ByteBufferUtil.debugAll(buffer);

        ByteBuffer buffer3 = ByteBuffer.wrap("hello".getBytes());
        ByteBufferUtil.debugAll(buffer);
    }
}
