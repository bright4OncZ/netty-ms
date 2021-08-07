package com.netty.c4;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.DefaultEventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.charset.Charset;

public class ReactorEventLoopServer {

    static Logger log = LoggerFactory.getLogger(ReactorEventLoopServer.class);

    public static void main(String[] args) {

        // boss 和 worker
        final NioEventLoopGroup bossGroup = new NioEventLoopGroup();
        // boss 只负责 ServerSocketChannel上的accept 事件, worker 只负责socketChannel 上的读写
        final NioEventLoopGroup workerGroup = new NioEventLoopGroup(2);

        // 创建一个独立的 EventLoopGroup
        final DefaultEventLoopGroup bizGroup = new DefaultEventLoopGroup();

        new ServerBootstrap()
                .group(bossGroup, workerGroup)
                .channel(NioServerSocketChannel.class)
                .childHandler(new ChannelInitializer<NioSocketChannel>() {
                    @Override
                    protected void initChannel(NioSocketChannel channel) throws Exception {
                        channel.pipeline().addLast("handler1", new ChannelInboundHandlerAdapter() {
                            @Override                  // ByteBuf
                            public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
                                final ByteBuf buf = (ByteBuf) msg;
                                // TODO 指定字符集
                                log.info(buf.toString(Charset.defaultCharset()));

                                // 让消息传递给下一个 handler
                                ctx.fireChannelRead(msg);
                            }
                        }).addLast(bizGroup, "handler2", new ChannelInboundHandlerAdapter() {
                            @Override                  // ByteBuf
                            public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
                                final ByteBuf buf = (ByteBuf) msg;
                                // TODO 指定字符集
                                log.info(buf.toString(Charset.defaultCharset()));
                            }
                        });
                    }
                })
                .bind(8080);
    }
}
