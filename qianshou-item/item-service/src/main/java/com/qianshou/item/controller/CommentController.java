package com.qianshou.item.controller;

import com.qianshou.Comment;
import com.qianshou.item.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import utlis.Result;

@RestController
public class CommentController {

    @Autowired
    private CommentService commentService;

    @GetMapping("/like/{id}")
    public Result likeComment(Long userId, @PathVariable("id") String publishId){
        if(userId==null || publishId==null){
            return Result.error().message("参数错误");
        }
        Long aLong = commentService.LikeComment(userId, publishId);
        return Result.ok().message("点赞成功").data("LikeCount",aLong);
    }

    @GetMapping("recommend/like/{id}")
    public Result removeLike(Long userId,@PathVariable("id") String publishId){
        Long aLong = commentService.removeLike(userId, publishId);
        if(aLong==null){
            return Result.error().message("取消失败请重试");
        }
        return Result.ok().data("removeLike",aLong);
    }

    @PostMapping("/commend/{id}")
    public Result commend(Long userId,@PathVariable("id") String publishId,String content){
        commentService.commend(userId,publishId,content);
        return Result.ok();
    }
}
