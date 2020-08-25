package com.qianshou.vod.controller;

import com.qianshou.vod.pojo.VodPublish;
import com.qianshou.vod.service.VodService;
import com.qianshou.vod.vo.CommentDetails;
import com.qianshou.vod.vo.VodInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import utlis.Result;

import java.util.List;

@RestController
@RequestMapping("/vod")
public class VodController {
    @Autowired
    private VodService vodService;

    @PostMapping("/save")
    public Result  addVod(@RequestParam("userId") Long userId, VodPublish vodPublish){
        if(null==userId){
            return Result.error().message("用户未登录");
        }
        Boolean aBoolean = vodService.saveVod(userId, vodPublish);
        return Result.ok();
    }

    @GetMapping("/getVod")
    public Result getVod(Integer page,Integer pageSize,Long userId){
        List<VodInfo> vod = this.vodService.getVod(page, pageSize,userId);
        return Result.ok().data("vod",vod);
    }

    @GetMapping("like/{id}")
    public Result likeComment(@PathVariable("id") String vodId,Long userId){
        Result result = vodService.likeComment(userId, vodId);
        return result;
    }

    @GetMapping("recommend/like/{id}")
    public Result removeLike(Long userId,@PathVariable("id") String publishId){
        Result result = vodService.removeComment(userId, publishId);
        if(result==null){
            return Result.error().message("取消失败请重试");
        }
        return Result.ok().data("removeLike",result);
    }

    @GetMapping("getCommentList")
    public List<CommentDetails> getCommentList(String publishId){
        List<CommentDetails> commentList = vodService.getCommentList(publishId);
        return commentList;
    }
}
