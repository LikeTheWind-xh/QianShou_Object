package com.qianshou.IM.service;

import com.alibaba.fastjson.JSON;
import com.qianshou.IM.Dao.MessageDao;
import com.qianshou.IM.client.UserClient;
import com.qianshou.IM.vo.MessageVo;
import com.qianshou.friend.pojo.Users;
import com.qianshou.pojo.Message;
import com.qianshou.pojo.UserInfo;
import javafx.scene.shape.Circle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
import utlis.Result;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class MessageService {
    @Autowired
    private UserClient userClient;

    @Autowired
    private MessageDao messageDao;

    @Autowired
    private MongoTemplate mongoTemplate;

    public List<MessageVo> getUserMessageList(Long userId){
        Query query = Query.query(Criteria.where("userId").is(userId));
        List<Users> users = this.mongoTemplate.find(query, Users.class);
        System.out.println(users);
        List<MessageVo> message = new ArrayList<>();
        List<Long> ids = new ArrayList<>();
        users.forEach(item->{
            List<Message> message1 = this.messageDao.queryUserList(item.getFriendId(), item.getUserId());
               if(message1!=null && !message1.isEmpty()){
                   Message message2 = message1.get(0);
                   MessageVo messageVo = new MessageVo();
                   messageVo.setContent(message2.getMsg());
                   messageVo.setFromId(message2.getFromUserId());
                   messageVo.setToId(message2.getToUserId());
                   messageVo.setCreate(message2.getSendDate());
                   messageVo.setUserId(item.getFriendId());
                  if(message2.getFromUserId()==userId){
                      messageVo.setStatus(2);
                  }else {
                      messageVo.setStatus(message2.getStatus());
                  }

                  if(!ids.contains(item.getFriendId())){
                      ids.add(item.getFriendId());
                  }
                  message.add(messageVo);
               }
        });

        if(ids!=null && !ids.isEmpty()){
            List<UserInfo> userInfos = userClient.findUserInfos(ids);
            userInfos.forEach(item->{
                message.forEach(msg->{
                    if(msg.getUserId()==item.getUserId()){
                        msg.setUserName(item.getNickName());
                        msg.setUserLogo(item.getLogo());
                        msg.setUserId(item.getUserId());
                    }
                });
            });
        }
        if(message!=null){
            return message;
        }
        return null;
    }
    public List<MessageVo> findMessageByToIdAndFromId(Long fromId, Long toId, Integer page, Integer pageSize){
        List<Message> messageList = messageDao.findByFromAndTo(fromId, toId, page, pageSize);
        List<Long> ids = new ArrayList<>();
        List<MessageVo> messageVoList = new ArrayList<>();
        messageList.forEach(item->{
            MessageVo messageVo = new MessageVo();
            messageVo.setId(item.getId().toString());
            messageVo.setStatus(item.getStatus());
            messageVo.setFromId(item.getFromUserId());
            messageVo.setToId(item.getToUserId());
            messageVo.setCreate(item.getSendDate());
            messageVo.setContent(item.getMsg());
            if(item.getStatus()==1){
                messageDao.updateMessageState(item.getId(),2);
            }
            if(!ids.contains(item.getFromUserId())){
                ids.add(item.getFromUserId());
            }
            messageVoList.add(messageVo);



        });
        List<UserInfo> users = userClient.findUserInfos(ids);

        users.forEach(item->{
            messageVoList.forEach(msg->{
                if(msg.getFromId() == item.getUserId()){
                   msg.setUserLogo(item.getLogo());
                   msg.setUserName(item.getNickName());
                }
            });
        });


        return messageVoList;

    }
}
