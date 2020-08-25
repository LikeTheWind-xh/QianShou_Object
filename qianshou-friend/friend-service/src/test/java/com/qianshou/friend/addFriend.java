package com.qianshou.friend;

import com.alibaba.fastjson.JSON;
import com.qianshou.friend.pojo.Users;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.test.context.junit4.SpringRunner;
import springfox.documentation.spring.web.json.Json;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SpringBootTest
@RunWith(SpringRunner.class)
public class addFriend {

    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired
    private FriendService friendService;

    @Test
    public void addOneUser(){
        friendService.addFriend(2L,1L);
//        friendService.consentFriendRequest(2L,1L);

    }

    @Test
    public void test(){
        Map map = new HashMap<String,Object>();
        map.put("tpOrderI","123456789");
        Map map1 = new HashMap<String,Object>();
        map1.put("name","鸭儿呦");
        map1.put("age",8);
        map1.put("sex","女");
        map.put("shop",map1);
        map.put("createTime",System.currentTimeMillis());
        String string = JSON.toJSONString(map);
        System.out.println(string);
    }

    @Test
    public void addUser(){

        for (long i=1;i<=15;i++){
            for (long j = 1; j <= 15; i++) {
                friendService.addFriend(i,j);
            }
        }
    }
}
