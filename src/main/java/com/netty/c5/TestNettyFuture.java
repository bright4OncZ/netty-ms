package com.netty.c5;

import io.netty.channel.EventLoop;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

public class TestNettyFuture {

    static Logger logger = LoggerFactory.getLogger(TestNettyFuture.class);

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        final NioEventLoopGroup group = new NioEventLoopGroup();

        final EventLoop eventLoop = group.next();
        final Future<Integer> future = eventLoop.submit(() -> {
            logger.info("执行计算 ...");
            TimeUnit.SECONDS.sleep(1);
            return 10;
        });

//        logger.info("等待结果");
//        final Integer result = future.get();
//        logger.info("结果是 {}", result);
        future.addListener(new GenericFutureListener<Future<? super Integer>>() {
            @Override
            public void operationComplete(Future<? super Integer> future) throws Exception {
                logger.info("结果是 {}", future.getNow());
            }
        });

    }
}
