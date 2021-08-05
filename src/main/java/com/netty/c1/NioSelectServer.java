package com.netty.c1;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.util.Iterator;


/**
 * Server 
 * @author cyp
 */
public class NioSelectServer {

    private static final Logger log = LoggerFactory.getLogger(NioSelectServer.class);

     public static void main(String[] args) throws IOException{

         // 使用nio 理解阻塞模式
        ByteBuffer buffer = ByteBuffer.allocate(16);

        // 1. 创建Selector, 可以管理多个channel
         final Selector selector = Selector.open();

         ServerSocketChannel ssc = ServerSocketChannel.open();
         ssc.configureBlocking(false);

         // 2. 建立channel 和 selector 联系
         // selectionKey 就是将来事件发生时, 通过它可以知道事件和哪个channel的事件
         final SelectionKey sscKey = ssc.register(selector, 0, null);
         log.debug("register key : {}", sscKey);
         sscKey.interestOps(SelectionKey.OP_ACCEPT);

         ssc.bind(new InetSocketAddress(8888));

        while (true) {
            // 3. select 方法, 没有事件发生, 线程阻塞, 有事件, 线程才会恢复运行
            selector.select();

            // 4. 处理事件
            final Iterator<SelectionKey> iterator = selector.selectedKeys().iterator();
            while (iterator.hasNext()) {
                final SelectionKey key = iterator.next();
                log.debug("key : {}", key);
                final ServerSocketChannel channel = (ServerSocketChannel) key.channel();
                final SocketChannel sc = channel.accept();
                log.debug("sc : {}", sc);

            }

        }
     }
    
}
