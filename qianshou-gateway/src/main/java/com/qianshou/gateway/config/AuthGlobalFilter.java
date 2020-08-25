package com.qianshou.gateway.config;

import com.alibaba.fastjson.JSON;
import com.qianshou.gateway.properties.FilterProperties;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Component
@EnableConfigurationProperties({FilterProperties.class})
public class AuthGlobalFilter implements GlobalFilter, Ordered {



    @Autowired
    private StringRedisTemplate redisTemplate;

    @Autowired
    private FilterProperties filterProperties;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        ServerHttpResponse response = exchange.getResponse();
        if(filterProperties.getUri().contains(request.getURI().getPath())){
            return chain.filter(exchange);
        }
        String token = request.getHeaders().getFirst("Authorization");
        if(StringUtils.isEmpty(token)){
            response.setStatusCode(HttpStatus.UNAUTHORIZED);
            return response.setComplete();
        }
        String getToken = redisTemplate.opsForValue().get("TOKEN_" + token);
        if(getToken==null){
            response.setStatusCode(HttpStatus.UNAUTHORIZED);
            return response.setComplete();
        }
        Map maps = JSON.parseObject(getToken, Map.class);
        Integer id = (Integer)maps.get("id");
        String userId = Integer.toString(id);
        ServerHttpRequest host = exchange.getRequest().mutate().header("userId",userId).build();
        exchange = exchange.mutate().request(host).build();
        redisTemplate.opsForValue().set("TOKEN_"+token,getToken,2, TimeUnit.HOURS);
        return chain.filter(exchange);
    }

    @Override
    public int getOrder() {
        return 0;
    }
}
