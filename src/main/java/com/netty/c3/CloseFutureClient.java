package com.netty.c3;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetSocketAddress;
import java.util.Scanner;

public class CloseFutureClient {

    static Logger log = LoggerFactory.getLogger(CloseFutureClient.class);

    public static void main(String[] args) throws InterruptedException {

        final NioEventLoopGroup group = new NioEventLoopGroup();
        ChannelFuture channelFuture = new Bootstrap()
                .group(group)
                .channel(NioSocketChannel.class)
                .handler(new ChannelInitializer<NioSocketChannel>() {
                    @Override
                    protected void initChannel(NioSocketChannel ch) throws Exception {
                        ch.pipeline().addLast(new LoggingHandler(LogLevel.DEBUG));
                        ch.pipeline().addLast(new StringEncoder());
                    }
                })
                .connect(new InetSocketAddress("localhost", 8080));


        final Channel channel = channelFuture.sync().channel();

        new Thread(() -> {
            final Scanner scanner = new Scanner(System.in);
            while (true) {
                final String line = scanner.nextLine();
                if ("q".equals(line)) {
                    channel.close(); // 异步操作
                    break;
                }
                channel.writeAndFlush(line);
            }
        }, "input").start();


        // 获取 CloseFuture 对象, 1. 同步处理关闭   2. 异步处理关闭
        final ChannelFuture closeFuture = channel.closeFuture();
//        log.info("waiting close ...");
//        closeFuture.sync();
//        log.debug("处理关闭之后的操作");

        closeFuture.addListener(new ChannelFutureListener() {
            @Override
            public void operationComplete(ChannelFuture future) throws Exception {
                log.info("处理关闭之后的操作");
                // 停止
                group.shutdownGracefully();
            }
        });

    }


}
