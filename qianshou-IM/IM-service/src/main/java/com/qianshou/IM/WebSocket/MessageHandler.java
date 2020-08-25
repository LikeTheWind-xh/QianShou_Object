package com.qianshou.IM.WebSocket;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.qianshou.IM.Dao.MessageDao;
import com.qianshou.IM.client.UserClient;
import com.qianshou.IM.vo.MessageVo;
import com.qianshou.pojo.Message;
import com.qianshou.pojo.UserInfo;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.*;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import javax.websocket.Session;
import java.util.*;

@Component
public class MessageHandler extends TextWebSocketHandler {

    private final Map<Long, WebSocketSession> SESSION = new HashMap<>();

    @Autowired
    private UserClient userClient;

    @Autowired
    private MessageDao messageDao;

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        Long userId = (Long) session.getAttributes().get("userId");
        SESSION.put(userId,session);
//        session.sendMessage();
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        Long userId = (Long) session.getAttributes().get("userId");
        JSONObject jsonObject = JSON.parseObject(message.getPayload());
        Integer ItoId = (Integer) jsonObject.get("toId");
        String string = ItoId.toString();
        Long toId = Long.valueOf(string);
        String msg = jsonObject.get("msg").toString();
        Message messageObject = Message.builder()
                .fromUserId(userId)
                .toUserId(toId)
                .msg(msg)
                .id(new ObjectId()).build();
        messageDao.saveMessage(messageObject);

        WebSocketSession webSocketSession = SESSION.get(toId);
        if(webSocketSession!=null&&webSocketSession.isOpen()){
            MessageVo messageVo = new MessageVo();
            messageVo.setFromId(userId);
            messageVo.setToId(toId);
            messageVo.setContent(msg);
            List<UserInfo> userInfos = userClient.findUserInfos(Arrays.asList(userId));
            UserInfo userInfo = userInfos.get(0);
            messageVo.setUserLogo(userInfo.getLogo());
            messageVo.setFromId(userId);
            messageVo.setUserName(userInfo.getNickName());
            webSocketSession.sendMessage(new TextMessage(JSON.toJSONBytes(messageVo)));
            messageDao.updateMessageState(messageObject.getId(),2);
        }
    }


    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        Long userId = (Long) session.getAttributes().get("userId");
        SESSION.remove(userId);
    }
}
