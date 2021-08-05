package com.netty.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringDecoder;

public class Server {

    public static void main(String[] args) {
        // 1. 启动器, 负责组装 netty 组件, 启动服务器
        new ServerBootstrap()
                // 2. BossEventLoop, WorkerEventLoop (selector, thread)  group 组
                .group(new NioEventLoopGroup())
                // 3. 选择服务器的 NioServerSocketChannel 实现
                .channel(NioServerSocketChannel.class)
                // 4. boss 负责处理连接, worker(child) 负责处理读写, worker 执行哪些操作 (handler)
                // 5. channel 代表和客户端进行数据读写通道  Initializer 初始化, 负责添加别的 handler
                .childHandler(new ChannelInitializer<NioSocketChannel>() {
                    @Override
                    protected void initChannel(NioSocketChannel channel) throws Exception {
                        // 6. 添加具体的 handler
                        channel.pipeline().addLast(new StringDecoder()); // 将ByteBuf 转换为字符串
                        channel.pipeline().addLast(new ChannelInboundHandlerAdapter() { // 自定义 handler
                            // 读事件
                            @Override
                            public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
                                System.out.println(msg);
                            }
                        });
                    }
                })
                // 7. 绑定服务器的端口
                .bind(8080);
    }
}
