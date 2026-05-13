package com.qims.quartz.config;

import com.qims.quartz.service.QuartzScheduleService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;

import java.util.Properties;

/**
 * Quartz 配置类
 * 使用内存存储任务（开发环境），生产环境可切换到数据库存储
 */
@Configuration
public class QuartzConfig {

    /**
     * 配置 SchedulerFactoryBean
     * 开发环境使用 RAMJobStore，生产环境可配置 JDBCJobStore
     */
    @Bean
    public SchedulerFactoryBean schedulerFactoryBean() {
        SchedulerFactoryBean factory = new SchedulerFactoryBean();
        
        // 配置属性 - 使用内存存储
        Properties properties = new Properties();
        properties.setProperty("org.quartz.scheduler.instanceName", "QIMS-Scheduler");
        properties.setProperty("org.quartz.scheduler.instanceId", "AUTO");
        
        // 使用内存存储（开发环境）
        // 生产环境改为: org.quartz.impl.jdbcjobstore.JobStoreTX
        properties.setProperty("org.quartz.jobStore.class", "org.quartz.simpl.RAMJobStore");
        
        properties.setProperty("org.quartz.threadPool.threadCount", "10");
        properties.setProperty("org.quartz.threadPool.threadPriority", "5");
        
        factory.setQuartzProperties(properties);
        
        // 延迟启动
        factory.setStartupDelay(10);
        factory.setOverwriteExistingJobs(true);
        factory.setAutoStartup(true);
        
        return factory;
    }

    /**
     * 初始化检测项定时任务
     * 在 Spring 容器完全初始化后执行
     */
    @Bean
    public org.springframework.boot.ApplicationRunner quartzInitializer(QuartzScheduleService quartzScheduleService) {
        return args -> {
            // 初始化时加载所有活跃的检测项任务
            quartzScheduleService.initJobs();
        };
    }
}