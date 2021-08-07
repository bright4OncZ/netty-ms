package com.netty.c5;

import io.netty.channel.EventLoop;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.util.concurrent.DefaultPromise;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

public class TestNettyPromise {

    static Logger logger = LoggerFactory.getLogger(TestNettyPromise.class);

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        // 1. 创建 eventLoop
        final EventLoop eventLoop = new NioEventLoopGroup().next();

        // 2. 主动创建 promise, 结果容器
        DefaultPromise<Integer> promise = new DefaultPromise<>(eventLoop);

        new Thread(() -> {
            // 3. 计算完, 往 promise 中 填充结果
            logger.info("开始执行逻辑 ...");
            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
                promise.setFailure(e);
            }

            promise.setSuccess(50);
        }).start();

        // 4. 接收结果的线程
        logger.info("等待结果 ...");
        logger.info("获取结果 {}", promise.get());
    }
}
