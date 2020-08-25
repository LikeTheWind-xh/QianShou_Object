package com.qianshou.api;


import com.qianshou.Comment;
import com.qianshou.Publish;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

public interface CircleApi {
    /**
     * 发布动态
     *
     * @param publish
     * @return
     */
    boolean savePublish(Publish publish);

    /**
     * 查询好友动态 id为空查询推荐动态
     *
     * @param userId
     * @param page
     * @param pageSize
     * @return
     */
    List<Publish> getPublish(Long userId, Integer page, Integer pageSize);

    /**
     * 点赞
     *
     * @param userId    用户id
     * @param publishId 帖子id
     * @return
     */
    boolean saveLikeComment(Long userId, String publishId);

    /**
     * 发布评论
     *
     * @param userId
     * @param publishId
     * @param content
     * @return
     */
    boolean saveComment(Long userId, String publishId,Integer commentType, String content);

    /**
     * 取消赞
     *
     * @param userId
     * @param publishId
     * @return
     */
    boolean removeComment(Long userId, String publishId, Integer commentType);

    /**
     * 查询指定类型总条数
     *
     * @param publishId
     * @param commentType 评论类型 1点赞 2评论
     * @return
     */
    Long queryCommentCount(String publishId, Integer commentType);

    Publish getPublishById(String id);

    List<Comment> getComment(String id);
}
