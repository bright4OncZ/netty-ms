package com.netty.netty.t1;

import com.netty.util.ByteBufferUtil;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.ByteBuffer;

public class TestByteBufferReadWrite {

    private static final Logger log = LoggerFactory.getLogger(TestByteBufferReadWrite.class);

    @Test
    public void test() {
        final ByteBuffer buffer = ByteBuffer.allocate(10);
        ByteBufferUtil.debugAll(buffer);
        buffer.put((byte) 0x61);
        ByteBufferUtil.debugAll(buffer);
        buffer.put(new byte[]{0x62, 0x63, 0x64});
        ByteBufferUtil.debugAll(buffer);

        log.info("read ==> [{}]", buffer.get());

        buffer.flip();
        log.info("read ==> [{}]", buffer.get());
        ByteBufferUtil.debugAll(buffer);
//        buffer.compact();
//        ByteBufferUtil.debugAll(buffer);
    }
}
