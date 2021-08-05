package com.netty.netty.t1;

import com.netty.util.ByteBufferUtil;
import org.junit.Test;

import java.nio.ByteBuffer;

public class TestByteBufferRead {

    @Test
    public void testRead() {
        final ByteBuffer buffer = ByteBuffer.allocate(10);
        buffer.put(new byte[]{'a', 'b', 'c', 'd'});

        buffer.flip();
        buffer.get(new byte[4]);
        ByteBufferUtil.debugAll(buffer);
        buffer.rewind();
        ByteBufferUtil.debugAll(buffer);
    }
}
