package com.qianshou.recommend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@SpringBootApplication
public class RecommendApplication {
    public static void main(String[] args) {
        SpringApplication.run(RecommendApplication.class);
    }
}
