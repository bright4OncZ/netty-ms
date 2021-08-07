package com.netty.c8;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.charset.StandardCharsets;
import java.util.Random;

public class FixLenClient {

    static Logger logger = LoggerFactory.getLogger(FixLenClient.class);

    public static void main(String[] args) {
//        for (int i = 0; i < 10; i++) {
//            send();
//        }
        send();
//        fill10Bytes('1', 5);
//        fill10Bytes('2', 2);
//        fill10Bytes('3', 10);
        System.out.println("finish");
    }

    public static byte[] fill10Bytes(char c, int len) {
        final byte[] result = new byte[10];
        if (len <= 0) {
            throw new IllegalArgumentException("len can not be 0");
        }
        int size = 10;
        if (len > size) {
            for (int i = 0; i < size; i++) {
                result[i] = (byte) c;
            }
        } else {
            for (int i = 0; i < len; i++) {
                result[i] = (byte) c;
            }
            for (int i = len; i < size; i++) {
                result[i] = (byte) '_';
            }
        }
        System.out.println(new String(result, StandardCharsets.UTF_8));

        return result;
    }

    private static void send() {
        final NioEventLoopGroup worker = new NioEventLoopGroup();
        try {
            final Bootstrap bootstrap = new Bootstrap();
            bootstrap.channel(NioSocketChannel.class);
            bootstrap.group(worker);
            bootstrap.handler(new ChannelInitializer<SocketChannel>() {
                @Override
                protected void initChannel(SocketChannel ch) throws Exception {
                    ch.pipeline().addLast(new LoggingHandler(LogLevel.DEBUG));
                    ch.pipeline().addLast(new ChannelInboundHandlerAdapter() {

                        @Override
                        public void channelActive(ChannelHandlerContext ctx) throws Exception {
                            ByteBuf buf = ctx.alloc().buffer();
                            char c = '0';
                            Random r = new Random();
                            for (int i = 0; i < 10; i++) {
                                final byte[] bytes = fill10Bytes(c, r.nextInt(10) + 1);
                                c++;
                                buf.writeBytes(bytes);
                            }
                            ctx.writeAndFlush(buf);
                        }
                    });
                }
            });
            final ChannelFuture channelFuture = bootstrap.connect("127.0.0.1", 8080).sync();
            channelFuture.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            logger.error("client error", e);
        } finally {
            worker.shutdownGracefully();
        }
    }
}
