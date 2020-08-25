package com.qianshou.IM;

import com.qianshou.IM.Dao.MessageDao;
import com.qianshou.pojo.Message;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

@SpringBootTest
@RunWith(SpringRunner.class)
public class testMessage {
    @Autowired
    private MessageDao messageDao;
    @Test
    public void testSend(){
        Message message = new Message();
        message.setFromUserId(2L);
        message.setToUserId(1L);
        message.setMsg("哇哦");
        messageDao.saveMessage(message);
//        Message message1 = new Message();
//        message1.setFromUserId(2L);
//        message1.setToUserId(1L);
//        message1.setMsg("可怜的小辉辉，缩头的小龟龟这场仗你输得好卑微");
//        messageDao.saveMessage(message);
//        messageDao.saveMessage(message1);
    }

    @Test
    public void testFindMessage(){

        List<Message> messages = messageDao.findByFromAndTo(1L, 2L, 1, 5);
        System.out.println(messages.toString());
    }
}
