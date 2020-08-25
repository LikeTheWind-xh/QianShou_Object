package com.qianshou.item.service;

import com.alibaba.fastjson.JSON;
import com.qianshou.Comment;
import com.qianshou.Publish;
import com.qianshou.api.CircleApi;
import com.qianshou.item.Dto.CommentDetails;
import com.qianshou.item.client.UserInfoClient;
import com.qianshou.item.Dto.PublishInfo;
import com.qianshou.pojo.UserInfo;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import utlis.Result;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Service
public class CircleService {

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Reference
    private CircleApi circleApi;
    @Autowired
    private UserInfoClient userInfoClient;

    private final String PUBLISH_ID = "PUBLISH_KEY";

    public Result getPublish(Long userId, Integer page, Integer pageSize) {
        List<Publish> publish = circleApi.getPublish(userId, page, pageSize);
        List<PublishInfo> publishInfoList = new ArrayList<>();
        publish.forEach(item -> {
            Result info = userInfoClient.findInfoById(item.getUserId());
            String s = JSON.toJSONString(info.getData());
            Map map = JSON.parseObject(s, Map.class);
            String s1 = JSON.toJSONString(map.get("userInfo"));
            UserInfo userInfo = JSON.parseObject(s1, UserInfo.class);
            PublishInfo publishInfo = new PublishInfo();
            publishInfo.setId(item.getId().toString());
            String isUserLike = "CIRCLE_COMMENT_LIKE_USER_" + userId + "_" + publishInfo.getId();
            publishInfo.setIsLike(this.stringRedisTemplate.hasKey(isUserLike));
            publishInfo.setLikeCount(circleApi.queryCommentCount(item.getId().toString(), 1));
            publishInfo.setCommentCount(circleApi.queryCommentCount(item.getId().toString(), 2));
            publishInfo.setPublish(item);
            publishInfo.setUserLogo(userInfo.getLogo());
            publishInfo.setUserNme(userInfo.getNickName());
            publishInfoList.add(publishInfo);
        });
        return Result.ok().data("data", publishInfoList);
    }

    public Boolean savePublish(Publish publish) {
        publish.setCreate(System.currentTimeMillis());
        return circleApi.savePublish(publish);
    }

    public PublishInfo getDetailPage(String publishId) {
        Publish publish = circleApi.getPublishById(publishId);
        Result user = userInfoClient.findInfoById(publish.getUserId());
        List<Comment> comments = circleApi.getComment(publishId);
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
}
