package com.qianshou.item.service;

import com.qianshou.api.CircleApi;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

@Service
public class CommentService {

    @Autowired
    private StringRedisTemplate redisTemplate;
    @Reference(timeout = 30000)
    private CircleApi circleApi;

    public Long LikeComment(Long userId, String publishId) {
        boolean flag = circleApi.saveLikeComment(userId, publishId);
        if(!flag){
            return null;
        }
        Long commentCount=0L;
        String likeCommentKey="CIRCLE_COMMENT_LIKE_"+publishId;
        if(!this.redisTemplate.hasKey(likeCommentKey)){
          commentCount  = circleApi.queryCommentCount(publishId, 1);
          redisTemplate.opsForValue().set(likeCommentKey,String.valueOf(commentCount));
        }else {
           commentCount =  redisTemplate.opsForValue().increment(likeCommentKey);
        }


        //记录是否用户点赞
        String isUserLike = "CIRCLE_COMMENT_LIKE_USER_"+userId+"_"+publishId;
        redisTemplate.opsForValue().set(isUserLike,"1");

        return commentCount;
    }

    public Long removeLike(Long userId, String publishId) {
        boolean b = circleApi.removeComment(userId, publishId, 1);
        if(!b){
            return null;
        }
        String likeCommentKey="CIRCLE_COMMENT_LIKE_"+publishId;
        Long aLong = redisTemplate.opsForValue().decrement(likeCommentKey);

        //取消用户点赞标记
        redisTemplate.delete("CIRCLE_COMMENT_LIKE_USER_"+userId+"_"+publishId);

        return aLong;
    }

    public Boolean commend(Long userId,String publishId,String content) {
        boolean flag = circleApi.saveComment(userId, publishId, 2, content);
        return flag;
    }
}
