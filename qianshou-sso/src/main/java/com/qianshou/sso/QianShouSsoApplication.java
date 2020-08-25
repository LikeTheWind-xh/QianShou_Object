package com.qianshou.sso;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.qianshou.sso")
public class QianShouSsoApplication {
    public static void main(String[] args) {
        SpringApplication.run(QianShouSsoApplication.class);
    }
}
