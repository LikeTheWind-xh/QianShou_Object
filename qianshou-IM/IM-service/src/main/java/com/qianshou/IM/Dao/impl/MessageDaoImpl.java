package com.qianshou.IM.Dao.impl;

import com.mongodb.client.result.UpdateResult;

import com.qianshou.IM.Dao.MessageDao;
import com.qianshou.pojo.Message;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

@Component
public class MessageDaoImpl implements MessageDao {

    @Autowired
    private MongoTemplate mongoTemplate;

    @Override
    public List<Message> findByFromAndTo(Long fromId, Long toId, Integer page, Integer rows) {
        Criteria criteriaFrom = new Criteria().andOperator(
          Criteria.where("fromUserId").is(fromId),
          Criteria.where("toUserId").is(toId)
        );

        Criteria criteriaTo = new Criteria().andOperator(
                Criteria.where("fromUserId").is(toId),
                Criteria.where("toUserId").is(fromId)
        );

        Criteria criteria = new Criteria().orOperator(criteriaFrom,criteriaTo);



        PageRequest pageRequest = PageRequest.of(page-1,rows, Sort.by(Sort.Direction.DESC,"send_date"));

        Query query = Query.query(criteria).with(pageRequest);
        List<Message> messageList = this.mongoTemplate.find(query, Message.class);
        return messageList;


    }

    @Override
    public Message findMessageById(String id) {
        return null;
    }

    @Override
    public Message queryList(Long fromId, Long toId) {
        Query query = Query.query(Criteria.where("fromUserId").is(fromId).and("toUserId").is(toId)).with(Sort.by(Sort.Direction.DESC,"send_date"));
        Message message = this.mongoTemplate.findOne(query, Message.class);
        return message;
    }
    @Override
    public List<Message> queryUserList(Long fromId, Long toId) {
        Criteria criteriaFrom = new Criteria().andOperator(
                Criteria.where("fromUserId").is(fromId),
                Criteria.where("toUserId").is(toId)
        );

        Criteria criteriaTo = new Criteria().andOperator(
                Criteria.where("fromUserId").is(toId),
                Criteria.where("toUserId").is(fromId)
        );

        Criteria criteria = new Criteria().orOperator(criteriaFrom,criteriaTo);

        PageRequest pageRequest = PageRequest.of(0,1, Sort.by(Sort.Direction.DESC,"send_date"));

        Query query = Query.query(criteria).with(pageRequest);
        List<Message> messageList = this.mongoTemplate.find(query, Message.class);
        return messageList;
    }
    @Override
    public UpdateResult updateMessageState(ObjectId id, Integer status) {
        Query query = Query.query(Criteria.where("id").is(id));
        Update update = Update.update("status",status);
        if(status==2){
            update.set("read_date",System.currentTimeMillis());
        }
        return this.mongoTemplate.updateFirst(query,update,Message.class);
    }

    @Override
    public Message saveMessage(Message message) {
        message.setSendDate(System.currentTimeMillis());
        message.setStatus(1);
        Message save = this.mongoTemplate.save(message);
        return save;

    }
}
