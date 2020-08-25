package com.qianshou.gateway.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

//@Configuration
public class GatewayCorsFilterConfiguration {

    @Bean
    public CorsFilter corsFilter(){
        //初始化配置对象
        CorsConfiguration configuration = new CorsConfiguration();

        //允许跨域的域名 可以设置多个 *代表所有  如果要携带cookie就不能使用*
        configuration.addAllowedOrigin("*");
        //允许携带cookie
        configuration.setAllowCredentials(true);

        //允许所有请求方式跨域访问
        configuration.addAllowedMethod("*");

        //允许携带所有头信息跨域访问
        configuration.addAllowedHeader("*");

        //初始化配置源对象
        UrlBasedCorsConfigurationSource configurationSource = new UrlBasedCorsConfigurationSource();

        //拦截所有路径 校验是否允许跨域
        configurationSource.registerCorsConfiguration("/**",configuration);

        return new CorsFilter(configurationSource);
    }
}
