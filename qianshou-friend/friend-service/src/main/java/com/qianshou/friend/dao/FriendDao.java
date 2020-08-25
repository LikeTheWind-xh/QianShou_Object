package com.qianshou.friend.dao;


import com.qianshou.friend.pojo.Users;
import javafx.scene.shape.Circle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public class FriendDao {

    @Autowired
    private MongoTemplate mongoTemplate;

    public Boolean addFriend(Long userId,Long friendId){
            Users users = Users.builder()
                    .userId(userId)
                    .friendId(friendId)
                    .isFriend(true).build();
            mongoTemplate.save(users);
            Users users1 = Users.builder()
                    .userId(friendId)
                    .friendId(userId)
                    .isFriend(false).build();
            mongoTemplate.save(users1);
            return true;
    }

    public Boolean consentFriendRequest(Long userId,Long friendId){

        Query query = Query.query(Criteria.where("friendId")
                .is(friendId).and("isFriend").is(false));

        Update update = Update.update("isFriend",true);
        mongoTemplate.updateFirst(query,update,Users.class);
        return true;
    }


    public List<Users> getAllRequest(Long userId) {
        Query query = Query.query(Criteria.where("friendId").is(userId).and("isFriend").is(false));
        List<Users> users = mongoTemplate.find(query, Users.class);
        return users;
    }

    public Users isFriend(Long userId,Long friendId){
        Criteria criteria = Criteria.where("userId").is(userId).and("friendId").is(friendId);
        Query query = Query.query(criteria);
        Users users = mongoTemplate.findOne(query, Users.class);
        return users;
    }
}
