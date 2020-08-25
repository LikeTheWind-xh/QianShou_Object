package com.qianshou.circle.service;

import com.alibaba.fastjson.JSON;
import com.mongodb.client.result.DeleteResult;
import com.qianshou.Comment;
import com.qianshou.FriendsList;
import com.qianshou.MyPublish;
import com.qianshou.Publish;
import com.qianshou.api.CircleApi;
import com.qianshou.friend.pojo.Users;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
import utlis.Result;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class CircleServiceImpl implements CircleApi {

    @Autowired
    private MongoTemplate mongoTemplate;

    @Override
    public boolean savePublish(Publish publish) {
        this.mongoTemplate.save(publish);
        //写入到个人发布表中
        MyPublish myPublish = new MyPublish();
        myPublish.setCreate(System.currentTimeMillis());
        myPublish.setPublishId(publish.getId());
        this.mongoTemplate.save(myPublish, "qianshou_circle_mypulish_" + publish.getUserId());

        //写入到好友列表中
        Query query = Query.query(Criteria.where("userId").is(publish.getUserId()));
        List<Users> users = this.mongoTemplate.find(query, Users.class);
        for (Users user : users) {
            FriendsList friendsList = new FriendsList();
            friendsList.setCreate(System.currentTimeMillis());
            friendsList.setPublishId(publish.getId());
            friendsList.setUserId(publish.getUserId());
            mongoTemplate.save(friendsList, "qianshou_circle_friendsList_" + user.getFriendId());
            System.out.println(user.getFriendId());
        }
        return true;
    }

    @Override
    public List<Publish> getPublish(Long userId, Integer page, Integer pageSize) {
        PageRequest pages = PageRequest.of(page - 1, pageSize, Sort.by(Sort.Order.desc("create")));
        Query query = new Query().with(pages);

        String collectionName="qianshou_circle_friendsList_";

        if(null == userId){
            collectionName+="recommend";
        }else {
            collectionName+=userId;
        }
        List<FriendsList> friendsLists = mongoTemplate.find(query, FriendsList.class, collectionName);
        List<ObjectId> ids = new ArrayList<>();
        for (FriendsList friendsList : friendsLists) {
            ids.add(friendsList.getPublishId());
        }

        Query query1 = Query.query(Criteria.where("id").in(ids)).with(Sort.by(Sort.Order.desc("create")));
        List<Publish> publishes = mongoTemplate.find(query1, Publish.class);

        return publishes;
    }

    @Override
    public boolean saveLikeComment(Long userId, String publishId) {
        Query query = Query.query(Criteria.where("userId").is(userId).and("publishId").is(new ObjectId(publishId)).and("commentType").is(1));
        long count = this.mongoTemplate.count(query, Comment.class);
        if(count>0){
            return false;
        }

        return this.saveComment(userId,publishId,1,null);
    }

    @Override
    public boolean saveComment(Long userId, String publishId,Integer commentType, String content) {
        Comment comment = new Comment();
        comment.setCommentType(commentType);
        comment.setPublishId(new ObjectId(publishId));
        comment.setUserId(userId);
        comment.setCreate(System.currentTimeMillis());
        comment.setContent(content);
        comment.setParent(true);
        mongoTemplate.save(comment);
        return true;
    }

    @Override
    public boolean removeComment(Long userId, String publishId ,Integer commentType) {
        Query query = Query.query(Criteria.where("userId").is(userId).and("publishId").is(new ObjectId(publishId)).and("commentType").is(commentType));
        DeleteResult remove = mongoTemplate.remove(query, Comment.class);
        long deletedCount = remove.getDeletedCount();
        return deletedCount>0;
    }

    @Override
    public Long queryCommentCount(String publishId, Integer commentType) {
        Query query = Query.query(Criteria.where("publishId").is(new ObjectId(publishId)).and("commentType").is(commentType));
        long count = mongoTemplate.count(query, Comment.class);
        return count;
    }

    @Override
    public Publish getPublishById(String id) {
        Query query = Query.query(Criteria.where("id").is(new ObjectId(id)));
        return mongoTemplate.findOne(query,Publish.class);
    }

    @Override
    public List<Comment> getComment(String id) {
        Query query = Query.query(Criteria.where("publishId").is(new ObjectId(id)).and("commentType").is(2));
        return mongoTemplate.find(query,Comment.class);
    }
}
