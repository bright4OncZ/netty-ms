package com.netty.c1;

import com.netty.util.ByteBufferUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.ArrayList;
import java.util.List;


public class NioServer {

    private static final Logger log = LoggerFactory.getLogger(NioServer.class);

     public static void main(String[] args) throws IOException{

         // 使用nio 理解阻塞模式
        ByteBuffer buffer = ByteBuffer.allocate(16);

        // 1. 创建服务器
         ServerSocketChannel ssc = ServerSocketChannel.open();
         // 切换为非阻塞模式, 线程还会运行
         ssc.configureBlocking(false);

        // 2. 绑定监听端口
        ssc.bind(new InetSocketAddress(8888));

        List<SocketChannel> channels = new ArrayList<>();

        while (true) {
            SocketChannel sc = ssc.accept(); // 非阻塞, 线程还会运行, 但sc 返回null
            if (sc != null) {
                log.debug("connected... {}", sc);
                // 非阻塞模式
                sc.configureBlocking(false);
                channels.add(sc);
            }

            for (SocketChannel channel : channels) {
                log.debug("before read... {}", channel);
                final int read = channel.read(buffer);
                if (read > 0) {
                    buffer.flip();
                    ByteBufferUtil.debugRead(buffer);
                    buffer.clear();
                    log.debug("after read... {}", sc);
                }
            }
        }
     }
    
}
