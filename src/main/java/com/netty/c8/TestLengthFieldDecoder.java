package com.netty.c8;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.channel.embedded.EmbeddedChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;

public class TestLengthFieldDecoder {

    public static void main(String[] args) {

        EmbeddedChannel channel = new EmbeddedChannel(
//                new LengthFieldBasedFrameDecoder(1024, 0, 4, 0, 0),
//                new LengthFieldBasedFrameDecoder(1024, 0, 4, 0, 4),
                new LengthFieldBasedFrameDecoder(1024, 0, 4, 1, 4),
                new LoggingHandler(LogLevel.DEBUG)
        );

        final ByteBuf buffer = ByteBufAllocator.DEFAULT.buffer();
        send(buffer, (byte) 1,"Hello, world");
        send(buffer, (byte) 1,"Hi");

        channel.writeInbound(buffer);
    }

    private static void send(ByteBuf buffer, byte version, String content) {
        byte[] bytes = content.getBytes();
        final int length = bytes.length;
        buffer.writeInt(length);
        buffer.writeByte(version);
        buffer.writeBytes(bytes);
    }
}
