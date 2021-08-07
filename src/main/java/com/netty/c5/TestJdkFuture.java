package com.netty.c5;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.*;

public class TestJdkFuture {

    static Logger logger = LoggerFactory.getLogger(TestJdkFuture.class);

    public static void main(String[] args) throws ExecutionException, InterruptedException {

        ExecutorService executorServices = Executors.newFixedThreadPool(2);

        final Future<Integer> future = executorServices.submit(() -> {
            logger.info("执行计算逻辑 ...");
            TimeUnit.SECONDS.sleep(1);
            return 50;
        });

        logger.info("等待结果 ...");
        final Integer result = future.get();
        logger.info("结果是 {}", result);

    }
}
