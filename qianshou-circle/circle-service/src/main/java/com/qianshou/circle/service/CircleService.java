package com.qianshou.circle.service;

import com.alibaba.fastjson.JSON;
import com.mongodb.client.result.DeleteResult;
import com.qianshou.Comment;
import com.qianshou.circle.vo.CommentDetails;
import com.qianshou.circle.vo.PublishInfo;
import com.qianshou.FriendsList;
import com.qianshou.MyPublish;
import com.qianshou.Publish;
import com.qianshou.circle.client.UserInfoClient;
import com.qianshou.friend.pojo.Users;
import com.qianshou.pojo.UserInfo;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import utlis.Result;

import java.security.Key;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

@Service
public class CircleService {
    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Autowired
    private UserInfoClient userInfoClient;
    /**
     * 发布帖子
     * @param publish
     * @return
     */
    public Boolean savePublish(Publish publish) {
        publish.setCreate(System.currentTimeMillis());
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


    /**
     * 获取帖子列表
     * @param userId
     * @param page
     * @param pageSize
     * @return
     */
    public Result getPublish(Long userId, Integer page, Integer pageSize) {
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
        System.out.println(ids);
        Query query1 = Query.query(Criteria.where("id").in(ids)).with(Sort.by(Sort.Order.desc("create")));
        List<Publish> publish = mongoTemplate.find(query1, Publish.class);
        List<PublishInfo> publishInfoList = new ArrayList<>();
        publish.forEach(item -> {
            Result info = userInfoClient.findInfoById(item.getUserId());
            String s = JSON.toJSONString(info.getData());
            Map map = JSON.parseObject(s, Map.class);
            String s1 = JSON.toJSONString(map.get("userInfo"));
            UserInfo userInfo = JSON.parseObject(s1, UserInfo.class);
            PublishInfo publishInfo = new PublishInfo();
            publishInfo.setId(item.getId().toString());
            String isUserLike = "COMMENT_LIKE_USER_" + userId + "_" + publishInfo.getId();
            publishInfo.setIsLike(this.redisTemplate.hasKey(isUserLike));
            publishInfo.setLikeCount(this.queryCommentCount(item.getId().toString(), 1));
            publishInfo.setCommentCount(this.queryCommentCount(item.getId().toString(), 2));
            publishInfo.setPublish(item);
            publishInfo.setUserLogo(userInfo.getLogo());
            publishInfo.setUserNme(userInfo.getNickName());
            publishInfoList.add(publishInfo);
        });
        return Result.ok().data("data", publishInfoList);
    }


    /**
     * 获取帖子详情包含评论内容和评论人
     * @param publishId
     * @return
     */
    public PublishInfo getDetailPage(String publishId) {
        Publish publish = this.getPublishById(publishId);
        Result user = userInfoClient.findInfoById(publish.getUserId());
        List<Comment> comments = this.getComment(publishId);
        List<CommentDetails> commentDetails = new LinkedList<>();
        if(!comments.isEmpty()){
            comments.forEach(item -> {
                CommentDetails commentDetails1 = new CommentDetails();
                commentDetails1.setComments(item);
                Result info = userInfoClient.findInfoById(item.getUserId());
                String s = JSON.toJSONString(info.getData());
                Map map = JSON.parseObject(s, Map.class);
                String s1 = JSON.toJSONString(map.get("userInfo"));
                UserInfo userInfo = JSON.parseObject(s1, UserInfo.class);
                commentDetails1.setUserName(userInfo.getNickName());
                commentDetails1.setUserLogo(userInfo.getLogo());
                commentDetails.add(commentDetails1);
            });
        }
        String info = JSON.toJSONString(user.getData().get("userInfo"));
        UserInfo userInfo = JSON.parseObject(info, UserInfo.class);
        PublishInfo publishInfo = new PublishInfo();
        publishInfo.setPublish(publish);
        publishInfo.setCommentDetails(commentDetails);
        publishInfo.setId(publishId);
        publishInfo.setUserLogo(userInfo.getLogo());
        publishInfo.setUserNme(userInfo.getNickName());
        return publishInfo;
    }

    /**
     * 保存喜欢
     * @param userId
     * @param publishId
     * @return
     */
    public Long saveLikeComment(Long userId, String publishId) {
        Query query = Query.query(Criteria.where("userId").is(userId).and("publishId").is(new ObjectId(publishId)).and("commentType").is(1));
        long count = this.mongoTemplate.count(query, Comment.class);
        if(count>0){
            return null;
        }
        boolean flag = this.saveComment(userId, publishId, 1, null);
        if(!flag){
            return null;
        }
        Long commentCount=0L;
        String likeCommentKey="COMMENT_LIKE_"+publishId;
        if(!this.redisTemplate.hasKey(likeCommentKey)){
            commentCount  = this.queryCommentCount(publishId, 1);
            System.out.println("likeCount:"+commentCount);
            redisTemplate.opsForValue().set(likeCommentKey,String.valueOf(commentCount));
        }else {
            commentCount =  redisTemplate.opsForValue().increment(likeCommentKey);
        }

        //记录是否用户点赞
        String isUserLike = "COMMENT_LIKE_USER_"+userId+"_"+publishId;
        redisTemplate.opsForValue().set(isUserLike,"1");

        return commentCount;
    }

    /**
     * 保存评论
     * @param userId
     * @param publishId
     * @param commentType
     * @param content
     * @return
     */
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

    /**
     * 移除喜欢
     * @param userId
     * @param publishId
     * @param commentType
     * @return
     */
    public Long removeComment(Long userId, String publishId ,Integer commentType) {
        Query query = Query.query(Criteria.where("userId").is(userId).and("publishId").is(new ObjectId(publishId)).and("commentType").is(commentType));
        DeleteResult remove = mongoTemplate.remove(query, Comment.class);
        long deletedCount = remove.getDeletedCount();
        Boolean b = deletedCount>0;
        if(!b){
            return null;
        }
        String likeCommentKey="COMMENT_LIKE_"+publishId;
        Long aLong = redisTemplate.opsForValue().decrement(likeCommentKey);

        //取消用户点赞标记
        redisTemplate.delete("COMMENT_LIKE_USER_"+userId+"_"+publishId);

        return aLong;
    }

    /**
     * 查询评论总数
     * @param publishId
     * @param commentType 1点赞 2评论
     * @return
     */
    public Long queryCommentCount(String publishId, Integer commentType) {
        Query query = Query.query(Criteria.where("publishId").is(new ObjectId(publishId)).and("commentType").is(commentType));
        long count = mongoTemplate.count(query, Comment.class);
        return count;
    }

    /**
     * 根据ID获取发布内容
     * @param id
     * @return
     */
    public Publish getPublishById(String id) {
        Query query = Query.query(Criteria.where("id").is(new ObjectId(id)));
        return mongoTemplate.findOne(query,Publish.class);
    }

    /**
     * 获取评论总数
     * @param id
     * @return
     */
    public List<Comment> getComment(String id) {
        Query query = Query.query(Criteria.where("publishId").is(new ObjectId(id)).and("commentType").is(2));
        return mongoTemplate.find(query,Comment.class);
    }

    public Result getMeAll(Long userId) {
        String tableName = "qianshou_circle_mypulish_"+userId;
        List<MyPublish> myPublishList = mongoTemplate.findAll(MyPublish.class, tableName);
        List<String> publishIds = new ArrayList<>();
        myPublishList.forEach(item->{
           publishIds.add(item.getId().toString());
        });
        Query query = Query.query(Criteria.where("id").in(publishIds)).with(Sort.by(Sort.Order.desc("create")));
        List<Publish> publishes = mongoTemplate.find(query, Publish.class);
        return Result.ok().data("publish",publishes);
    }
}
