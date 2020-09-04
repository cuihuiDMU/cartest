package com.cartest.pro;

import org.apache.log4j.Logger;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.PropertySource;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EnableTransactionManagement
@EnableAsync
@ServletComponentScan
@MapperScan(value = "com.cartest.pro.mapper")
@ComponentScan(basePackages = {"com.cartest.pro"})
@PropertySource(value = "classpath:/default.properties")
public class ProApplication {

    public static void main(String[] args) {
        SpringApplication.run(ProApplication.class, args);
        System.out.println("======== MainApplication started success ========");
    }

}
