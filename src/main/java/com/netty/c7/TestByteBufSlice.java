package com.netty.c7;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;

import static io.netty.buffer.ByteBufUtil.appendPrettyHexDump;
import static io.netty.util.internal.StringUtil.NEWLINE;

/**
 * slice
 * 零拷贝的体现之一, 对原始 ByteBuf 进行切片成多个 ByteBuf , 切片后的 ByteBuf 并没有发生内存复制
 * 还是使用原始的 ByteBuf 的内存, 切片后的 ByteBuf 维护独立的 read, write 指针
 * duplicate
 *
 */
public class TestByteBufSlice {

    public static void main(String[] args) {

        final ByteBuf buf = ByteBufAllocator.DEFAULT.buffer(10);
        buf.writeBytes(new byte[]{'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j'});

        log(buf);

        final ByteBuf s1 = buf.slice(0, 5);
        final ByteBuf s2 = buf.slice(5, 5);

        log(s1);
        log(s2);

        System.out.println("============================");
        s1.setByte(0, 'b');
        log(s1);
        log(buf);

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
