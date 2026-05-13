package com.qims.web;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * QIMS 质量管理系统启动类
 */
@SpringBootApplication(scanBasePackages = "com.qims")
@MapperScan("com.qims.domain.mapper")
@EnableAsync
@EnableScheduling
public class QualityApplication {

    public static void main(String[] args) {
        SpringApplication.run(QualityApplication.class, args);
    }
}
