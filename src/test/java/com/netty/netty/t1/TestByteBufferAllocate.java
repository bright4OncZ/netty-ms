package com.netty.netty.t1;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.ByteBuffer;

public class TestByteBufferAllocate {

    private static final Logger log = LoggerFactory.getLogger(TestByteBufferAllocate.class);

    @Test
    public void test() {
        final Class<? extends ByteBuffer> class1 = ByteBuffer.allocate(16).getClass();
        final Class<? extends ByteBuffer> class2 = ByteBuffer.allocateDirect(16).getClass();
        log.info("allocate ==> [{}]", class1);
        log.info("allocateDirect ==> [{}]", class2);
    }
}
