package com.netty.c4;

import io.netty.channel.nio.NioEventLoopGroup;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;

public class TestEventLoop {

    static Logger log = LoggerFactory.getLogger(TestEventLoop.class);

    public static void main(String[] args) {
        // 1. 创建事件循环组  NioEventLoopGroup  io 事件， 普通任务, 定时任务
        final NioEventLoopGroup group = new NioEventLoopGroup(2);
        // 2. 获取下一个事件循环对象
        System.out.println(group.next());
        System.out.println(group.next());
        System.out.println(group.next());
        System.out.println(group.next());

        // 3. 执行普通任务
//        group.next().execute(() -> {
//            try {
//                TimeUnit.SECONDS.sleep(1);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//            log.info("ok");
//        });

        // 4. 执行定时任务
        group.next().scheduleAtFixedRate(() -> {
            log.info("ok");
        }, 1, 2, TimeUnit.SECONDS);

        log.info("main ");
    }
}
