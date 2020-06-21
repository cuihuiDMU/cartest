package com.cartest.pro;

import org.apache.log4j.Logger;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication

public class ProApplication {

    public static void main(String[] args) {
        SpringApplication.run(ProApplication.class, args);
        System.out.println("======== MainApplication started success ========");
    }

}
