package com.netty.c6;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TestPipeline {

    static Logger logger = LoggerFactory.getLogger(TestPipeline.class);

    public static void main(String[] args) {
        new ServerBootstrap()
                .group(new NioEventLoopGroup())
                .channel(NioServerSocketChannel.class)
                .childHandler(new ChannelInitializer<NioSocketChannel>() {
                    @Override
                    protected void initChannel(NioSocketChannel ch) throws Exception {
                        // 1. 通过 channel 获取 pipeline
                        final ChannelPipeline pipeline = ch.pipeline();

                        // 2. 添加处理器 head -> h1 -> h2 -> h3 -> tail
                        pipeline.addLast("h1", new ChannelInboundHandlerAdapter() {
                            @Override
                            public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
                                logger.info("1");
                                super.channelRead(ctx, msg);
                            }
                        });

                        pipeline.addLast("h2", new ChannelInboundHandlerAdapter() {
                            @Override
                            public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
                                logger.info("2");
                                super.channelRead(ctx, msg);
                            }
                        });


                        pipeline.addLast("h3", new ChannelInboundHandlerAdapter() {
                            @Override
                            public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
                                logger.info("3");
                                super.channelRead(ctx, msg);
                                ch.writeAndFlush(ctx.alloc().buffer().writeBytes("server ...".getBytes()));
                            }
                        });

                        pipeline.addLast("h4", new ChannelOutboundHandlerAdapter() {

                            @Override
                            public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
                                logger.info("4");
                                super.write(ctx, msg, promise);
                            }
                        });

                        pipeline.addLast("h5", new ChannelOutboundHandlerAdapter() {

                            @Override
                            public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
                                logger.info("5");
                                super.write(ctx, msg, promise);
                            }
                        });

                        pipeline.addLast("h6", new ChannelOutboundHandlerAdapter() {

                            @Override
                            public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
                                logger.info("6");
                                super.write(ctx, msg, promise);
                            }
                        });


                    }
                })
                .bind(8080);

    }
}
