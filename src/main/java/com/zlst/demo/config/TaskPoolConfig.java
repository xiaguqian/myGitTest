package com.zlst.demo.config;


import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;

@Configuration
@EnableAsync  //开启异步操作
public class TaskPoolConfig implements AsyncConfigurer {

    /**
     * 通过getAsyncExecutor方法配置ThreadPoolTaskExecutor,获得一个基于线程池TaskExecutor
     *
     * @return
     */
    @Override
    public Executor getAsyncExecutor() {
        ThreadPoolTaskExecutor pool = new ThreadPoolTaskExecutor();
        pool.setCorePoolSize(5);//核心线程数
        pool.setMaxPoolSize(10);//最大线程数
        pool.setQueueCapacity(25);//线程队列
        pool.initialize();//线程初始化
        return pool;
    }

    @Override
    public AsyncUncaughtExceptionHandler getAsyncUncaughtExceptionHandler() {
        return null;
    }
//    @Bean
//    public MyMaps getMyMaps(){
//        return new MyMaps();
//    }
}
//@EnableAsync
//@Configuration
//public class TaskPoolConfig {
//    @Bean("taskExector")
//    public TaskExecutor taskExector() {
//
//        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
//        int i = Runtime.getRuntime().availableProcessors();//获取到服务器的cpu内核
//        executor.setCorePoolSize(5);//核心池大小
//        executor.setMaxPoolSize(100);//最大线程数
//        executor.setQueueCapacity(1000);//队列程度
//        executor.setKeepAliveSeconds(1000);//线程空闲时间
//        executor.setThreadNamePrefix("tsak-asyn");//线程前缀名称
//        executor.setWaitForTasksToCompleteOnShutdown(true);
//        executor.setAwaitTerminationSeconds(3);
//        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.AbortPolicy());//配置拒绝策略
//        return executor;
//    }
//}
