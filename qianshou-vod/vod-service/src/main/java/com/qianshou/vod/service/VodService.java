package com.qianshou.vod.service;

import com.alibaba.fastjson.JSON;
import com.qianshou.Comment;
import com.qianshou.pojo.UserInfo;
import com.qianshou.vod.client.CommentClient;
import com.qianshou.vod.client.UserInfoClient;
import com.qianshou.vod.pojo.VodPublish;
import com.qianshou.vod.vo.CommentDetails;
import com.qianshou.vod.vo.VodInfo;
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

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class VodService {
    @Autowired
    private UserInfoClient userInfoApi;

    @Autowired
    private CommentClient commentClient;

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Autowired
    private MongoTemplate mongoTemplate;
    public Boolean saveVod(Long userId,VodPublish vodPublish){
        vodPublish.setUserId(userId);
        vodPublish.setCreate(System.currentTimeMillis());
        mongoTemplate.save(vodPublish);
        return true;
    }

    public List<VodInfo> getVod(Integer page,Integer pageSize,Long userId){
        PageRequest pageRequest = PageRequest.of(page-1,pageSize, Sort.by(Sort.Order.desc("create")));
        Query query = new Query().with(pageRequest);
        List<VodPublish> publishes = mongoTemplate.find(query, VodPublish.class);
        List<VodInfo> vodInfoList = new ArrayList<>();
        publishes.forEach(item->{
            Result info = userInfoApi.findInfoById(item.getUserId());
            String s = JSON.toJSONString(info.getData());
            Map map = JSON.parseObject(s, Map.class);
            String s1 = JSON.toJSONString(map.get("userInfo"));
            UserInfo userInfo = JSON.parseObject(s1, UserInfo.class);

            VodInfo vodInfo = new VodInfo();

            vodInfo.setId(item.getId().toString());
            vodInfo.setCommentCount(commentClient.queryCommentCount(item.getId().toString(), 2));
            System.out.println(item.getId().toString());
            String isUserLike = "COMMENT_LIKE_USER_" + userId + "_" + item.getId().toString();
            vodInfo.setIsLike(this.redisTemplate.hasKey(isUserLike));
            vodInfo.setLikeCount(commentClient.queryCommentCount(item.getId().toString(),1));
            vodInfo.setContent(item.getText());
            vodInfo.setUserLogo(userInfo.getLogo());
            vodInfo.setUserId(userInfo.getUserId());
            vodInfo.setUserName(userInfo.getNickName());
            vodInfo.setVodUrl(item.getVodUrl());
            vodInfo.setCoverUrl(item.getCoverUrl());
            vodInfoList.add(vodInfo);
        });
        return vodInfoList;
    }

    public Result likeComment(Long userId,String publishId){
        Result result = commentClient.likeComment(userId, publishId);
        System.out.println(result);
        return result;
    }

    public Result removeComment(Long userId, String publishId) {
        Result result = commentClient.removeLike(userId, publishId);
        return result;
    }

    public List<CommentDetails> getCommentList(String publishId) {
        Query query = Query.query(Criteria.where("publishId").is(new ObjectId(publishId)).and("commentType").is(2)).with(Sort.by(Sort.Order.desc("create")));

        List<Comment> comments = this.mongoTemplate.find(query, Comment.class);
        List<CommentDetails> commentDetailsList = new ArrayList<>();
        List<Long> ids = new ArrayList<>();
        if(comments!=null&&!comments.isEmpty()){
            comments.forEach(item->{
                CommentDetails commentDetails = new CommentDetails();
                commentDetails.setCreate(item.getCreate());
                commentDetails.setContent(item.getContent());
                commentDetails.setUserId(item.getUserId());
                ids.add(item.getUserId());
                commentDetailsList.add(commentDetails);
            });

            List<UserInfo> userInfos = userInfoApi.findUserInfos(ids);
            userInfos.forEach(item->{
                commentDetailsList.forEach(comment->{
                    if(comment.getUserId()==item.getUserId()){

                        comment.setUserLogo(item.getLogo());
                        comment.setUserName(item.getNickName());
                    }
                });
            });

            return commentDetailsList;
        }
        return null;
    }
}
