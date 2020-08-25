package com.qianshou.IM;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.cloud.openfeign.EnableFeignClients;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@EnableFeignClients
@EnableSwagger2
@SpringBootApplication(exclude = DataSourceAutoConfiguration.class)
public class IMApplication {
    public static void main(String[] args) {
        SpringApplication.run(IMApplication.class);
    }
}
