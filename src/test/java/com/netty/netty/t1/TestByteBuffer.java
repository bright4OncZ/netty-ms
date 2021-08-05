package com.netty.netty.t1;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

public class TestByteBuffer {

    private static final Logger log = LoggerFactory.getLogger(TestByteBuffer.class);

    @Test
    public void testByteBuff() {
        try (FileChannel channel = new FileInputStream("data.txt").getChannel()) {
            ByteBuffer buffer = ByteBuffer.allocate(10);
            while (true) {
                int len = channel.read(buffer);
                log.debug("读取到的字节数 [{}]", len);
                if (len == -1) {
                    break;
                }
                // 切换为读模式
                buffer.flip();

                while (buffer.hasRemaining()) {
                    final byte b = buffer.get();
                    log.debug("实际字节 [{}]", (char) b);
                }
                // 切换为写模式
                buffer.clear();
            }
        } catch (IOException e) {

        }
    }

    @Test
    public void testByteBufferRead() {
        try (FileChannel channel = new FileInputStream("data.txt").getChannel()) {
            ByteBuffer buffer = ByteBuffer.allocate(10);
            channel.read(buffer);

            buffer.flip();

            while (buffer.hasRemaining()) {
                final byte b = buffer.get();
                log.info("读取到的字节数 [{}]", (char) b);
            }
            // 切换为写模式
            buffer.clear();
        } catch (IOException e) {

        }
    }
}
