package com.lb.lbapp.config;

import java.util.concurrent.Executor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

@Configuration
@EnableAsync
@ComponentScan("com.lb.lbapp")
public class SpringAsyncConfig implements AsyncConfigurer {

	Logger logger = LoggerFactory.getLogger(SpringAsyncConfig.class);
	
	@Bean(name = "threadPoolTaskExecutor")
	public Executor threadPoolTaskExecutor() {

		logger.info("ThreadPoolTaskExecutor Created");
		ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
		executor.setCorePoolSize(6);
		executor.setMaxPoolSize(15);
		executor.setQueueCapacity(3);
		executor.initialize();
		
		System.out.printf("ThreadPoolTaskExecutor Properties -corePoolSize:%s; maxPoolSize:%s; "
				+ "poolSize:%s; activeCount:%s;",executor.getCorePoolSize(),executor.getMaxPoolSize(),
				executor.getPoolSize(),executor.getActiveCount());
		
		return executor;
	}
	
	@Override
    public AsyncUncaughtExceptionHandler getAsyncUncaughtExceptionHandler() {
        return new CustomAsyncExceptionHandler();
    }

}
