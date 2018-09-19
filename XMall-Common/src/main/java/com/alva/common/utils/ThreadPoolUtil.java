package com.alva.common.utils;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * <一句话描述>,
 * <详细介绍>,
 *
 * @author 穆国超
 * @since 设计wiki | 需求wiki
 */
public class ThreadPoolUtil {

    //线程缓冲队列
    private static BlockingQueue<Runnable> blockingQueue = new ArrayBlockingQueue<>(100);

    //核心线程数
    private static final int SIZE_CORE_POOL = 5;

    //线程池维护线程的最大数量
    private static final int SIZE_MAX_POOL = 10;

    //线程池维护线程所允许的空闲时间
    private static final long ALIVE_TIME = 2000;

    private static ThreadPoolExecutor pool = new ThreadPoolExecutor(SIZE_CORE_POOL, SIZE_MAX_POOL, ALIVE_TIME, TimeUnit.MILLISECONDS, blockingQueue, new ThreadPoolExecutor.CallerRunsPolicy());

    static {
        pool.prestartAllCoreThreads();
    }

    public static ThreadPoolExecutor getPool() {
        return pool;
    }

    public static void main(String[] args) {
        System.out.println(pool.getPoolSize());
    }
}
