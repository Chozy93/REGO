package com.itwillbs;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.itwillbs.mapper")
public class RegoApplication {
    public static void main(String[] args) {
        SpringApplication.run(RegoApplication.class, args);
    }
}
