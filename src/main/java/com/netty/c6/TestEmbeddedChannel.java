package com.netty.c6;

import io.netty.buffer.ByteBufAllocator;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelOutboundHandlerAdapter;
import io.netty.channel.ChannelPromise;
import io.netty.channel.embedded.EmbeddedChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TestEmbeddedChannel {

    static Logger logger = LoggerFactory.getLogger(TestEmbeddedChannel.class);

    public static void main(String[] args) {
        final ChannelInboundHandlerAdapter h1 = new ChannelInboundHandlerAdapter() {
            @Override
            public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
                logger.info("1");
                super.channelRead(ctx, msg);
            }
        };

        final ChannelInboundHandlerAdapter h2 = new ChannelInboundHandlerAdapter() {
            @Override
            public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
                logger.info("2");
                super.channelRead(ctx, msg);
            }
        };

        final ChannelOutboundHandlerAdapter h3 = new ChannelOutboundHandlerAdapter() {
            @Override
            public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
                logger.info("3");
                super.write(ctx, msg, promise);
            }
        };


        final ChannelOutboundHandlerAdapter h4 = new ChannelOutboundHandlerAdapter() {
            @Override
            public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
                logger.info("4");
                super.write(ctx, msg, promise);
            }
        };

        final EmbeddedChannel channel = new EmbeddedChannel(h1, h2, h3, h4);
        // 模拟入站操作
        channel.writeInbound(ByteBufAllocator.DEFAULT.buffer().writeBytes("hello".getBytes()));

        // 摸拟出站操作
        channel.writeOutbound(ByteBufAllocator.DEFAULT.buffer().writeBytes("world".getBytes()));
    }
}
