package com.qianshou.IM.WebSocket;

import com.alibaba.fastjson.JSON;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.server.HandshakeInterceptor;

import java.util.Map;
@Component
public class WebSocketHandlerHandshakeInterceptor implements HandshakeInterceptor {
    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Override
    public boolean beforeHandshake(ServerHttpRequest serverHttpRequest, ServerHttpResponse serverHttpResponse, org.springframework.web.socket.WebSocketHandler webSocketHandler, Map<String, Object> map) throws Exception {
        String authorization = serverHttpRequest.getHeaders().getFirst("Authorization");
        String token = stringRedisTemplate.opsForValue().get("TOKEN_" + authorization);
        System.out.println("进来");
        if(token!=null){
            Map maps = JSON.parseObject(token, Map.class);
            Integer integer = (Integer) maps.get("id");
            String toString = integer.toString();
            Long userId = Long.valueOf(toString);
            map.put("userId",userId);
            return true;
        }

        return false;
    }

    @Override
    public void afterHandshake(ServerHttpRequest serverHttpRequest, ServerHttpResponse serverHttpResponse, org.springframework.web.socket.WebSocketHandler webSocketHandler, Exception e) {

    }
}
