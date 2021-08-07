package com.netty.c4;


import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringEncoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetSocketAddress;

/**
 * channel 的主要作用
 * 1. close() 可以用来关闭 channel
 * 2. closeFuture() 用来处理 channel 的关闭
 *      sync 方法作用是同步等待 channel 关闭
 *      而 addListener 方法是异步等待 channel 关闭
 * 3. pipeline() 方法添加处理器
 * 4. write() 方法将数据写入
 * 5. writeAndFlush() 方法将数据写入并刷出
 */
public class ChannelClient {

    static Logger logger = LoggerFactory.getLogger(ChannelClient.class);

    public static void main(String[] args) throws InterruptedException {

        // 2. 带有 Future, Promise 的类型都是和异步方法配套使用, 用来处理结果
        final ChannelFuture channelFuture = new Bootstrap()
                .group(new NioEventLoopGroup())
                .channel(NioSocketChannel.class)
                .handler(new ChannelInitializer<NioSocketChannel>() {
                    @Override
                    protected void initChannel(NioSocketChannel channel) throws Exception {
                        channel.pipeline().addLast(new StringEncoder());
                    }
                })
                // 1. 连接到服务器, connect 异步非阻塞, main 发起调用, 真正执行的 connect 是 nio 线程
                .connect(new InetSocketAddress("localhost", 8080));

        // 2.1 使用 sync 方法同步处理结果
//        channelFuture.sync();  // 阻塞当前线程, 直到 nio 线程连接建立完毕
//        final Channel channel = channelFuture.channel();
//        logger.info("channel ==> {}", channel);
//        channel.writeAndFlush("hello, world");


        // 2.2 使用 addListener(回调对象) 方法异步处理结果
        channelFuture.addListener(new ChannelFutureListener() {
            // 在 nio 线程连接建立好之后, 会调用 operationComplete
            @Override
            public void operationComplete(ChannelFuture channelFuture) throws Exception {
                channelFuture.sync();  // 阻塞当前线程, 直到 nio 线程连接建立完毕
                final Channel channel = channelFuture.channel();
                logger.info("channel ==> {}", channel);
                channel.writeAndFlush("hello, world");
            }
        });

    }
}
