package com.qianshou.api;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import utlis.Result;

public interface CommentApi {
    @GetMapping("/comment/like/{id}")
    Result likeComment(@RequestParam("userId") Long userId, @PathVariable("id") String publishId);

    @GetMapping("/comment/recommend/like/{id}")
    Result removeLike(@RequestParam("userId")Long userId,@PathVariable("id") String publishId);

    @PostMapping("/comment/{id}")
    Result commend(@RequestParam("userId") Long userId,@PathVariable("id") String publishId,String content);

    @GetMapping("/comment/query/queryCommentCount")
    Long queryCommentCount(@RequestParam("publishId")String publishId, @RequestParam("commentType")Integer commentType);
}
