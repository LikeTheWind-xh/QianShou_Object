package com.qianshou.IM.WebSocket;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;


//开启webSocket
@Component
@EnableWebSocket
public class WebSocketConfig implements WebSocketConfigurer {

    @Autowired
    private WebSocketHandlerHandshakeInterceptor webSocketHandlerHandshakeInterceptor;

    @Autowired
    private MessageHandler messageHandler;

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry webSocketHandlerRegistry) {
        //添加消息处理器,和拦截器
        webSocketHandlerRegistry.addHandler(messageHandler,"/qianshou/IM").setAllowedOrigins("*").addInterceptors(webSocketHandlerHandshakeInterceptor);
    }
}
