package com.netty.c7;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.buffer.CompositeByteBuf;

import static io.netty.buffer.ByteBufUtil.appendPrettyHexDump;
import static io.netty.util.internal.StringUtil.NEWLINE;

/**
 * ByteBuf 优势
 * 池化, 可用重用池中 ByteBuf 实例, 更节约内存, 减少内存溢出的可能
 * 读写指针分离, 不需要像 java nio ByteBuffer 一样切换读写模式
 * 可以自动扩容
 * 支持链式调用, 使用更流畅
 * 很多地方体现零拷贝
 */
public class TestCompositeByteBuf {

    public static void main(String[] args) {
        final ByteBuf buf1 = ByteBufAllocator.DEFAULT.buffer();
        buf1.writeBytes(new byte[]{1, 2, 3, 4, 5});

        final ByteBuf buf2 = ByteBufAllocator.DEFAULT.buffer();
        buf2.writeBytes(new byte[]{6, 7, 8, 9, 10});

//        final ByteBuf buffer = ByteBufAllocator.DEFAULT.buffer();
        // writeBytes 发生复制, 这里发生两次复制
//        buffer.writeBytes(buf1).writeBytes(buf2);
//        log(buffer);
        final CompositeByteBuf buffer = ByteBufAllocator.DEFAULT.compositeBuffer();
//        buffer.addComponents(buf1, buf2);
        buffer.addComponents(true, buf1, buf2);
        log(buffer);

    }

    private static void log(ByteBuf buffer) {
        int length = buffer.readableBytes();
        int rows = length / 16 + (length % 15 == 0 ? 0 : 1) + 4;
        StringBuilder buf = new StringBuilder(rows * 80)
                .append("read index:").append(buffer.readerIndex())
                .append(" write index:").append(buffer.writerIndex())
                .append(" capacity:").append(buffer.capacity())
                .append(NEWLINE);
        appendPrettyHexDump(buf, buffer);
        System.out.println(buf.toString());
    }
}
