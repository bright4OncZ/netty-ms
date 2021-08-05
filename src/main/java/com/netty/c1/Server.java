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


public class Server {

    private static final Logger log = LoggerFactory.getLogger(Server.class);

     public static void main(String[] args) throws IOException{

         // 使用nio 理解阻塞模式
        ByteBuffer buffer = ByteBuffer.allocate(16);

        // 1. 创建服务器
        ServerSocketChannel ssc = ServerSocketChannel.open();

        // 2. 绑定监听端口
        ssc.bind(new InetSocketAddress(8888));

        List<SocketChannel> channels = new ArrayList<>();

        while (true) {
            log.debug("connecting...");
            SocketChannel sc = ssc.accept();
            log.debug("connected... {}", sc);
            channels.add(sc);

            for (SocketChannel channel : channels) {
                log.debug("before read... {}", channel);
                channel.read(buffer);
            }

            buffer.flip();
            ByteBufferUtil.debugRead(buffer);
            buffer.clear();
            log.debug("after read... {}", sc);
        }
     }
    
}
