package com.qianshou.circle.controller;

import com.qianshou.Comment;
import com.qianshou.circle.service.CircleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import utlis.Result;

@RestController
@RequestMapping("/comment")
public class CommentController {

    @Autowired
    private CircleService circleService;

    @GetMapping("/like/{id}")
    public Result likeComment(Long userId, @PathVariable("id") String publishId){
        if(userId==null || publishId==null){
            return Result.error().message("参数错误");
        }
        Long aLong = circleService.saveLikeComment(userId, publishId);
        System.out.println(aLong);
        return Result.ok().message("点赞成功").data("LikeCount",aLong);
    }

    @GetMapping("recommend/like/{id}")
    public Result removeLike(Long userId,@PathVariable("id") String publishId){
        Long aLong = circleService.removeComment(userId, publishId,1);
        if(aLong==null){
            return Result.error().message("取消失败请重试");
        }
        return Result.ok().data("removeLike",aLong);
    }

    @PostMapping("/comment/{id}")
    public Result commend(Long userId,@PathVariable("id") String publishId,String content){
        circleService.saveComment(userId,publishId,2,content);
        return Result.ok();
    }

    @GetMapping("/query/queryCommentCount")
   public Long queryCommentCount(String publishId, Integer commentType){
        System.out.println(publishId+"-----------"+commentType);
        Long aLong = circleService.queryCommentCount(publishId, commentType);
        return aLong;
    }
}
