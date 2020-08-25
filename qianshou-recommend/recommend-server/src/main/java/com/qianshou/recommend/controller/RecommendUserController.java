package com.qianshou.recommend.controller;

import com.qianshou.recommend.pojo.qianshouUser;
import com.qianshou.recommend.service.RecommendUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import utlis.Result;

@RestController
public class RecommendUserController {

    @Autowired
    private RecommendUser recommendUser;

    @GetMapping("getRecommend")
    public qianshouUser getRecommendById(Long id){
        qianshouUser user = recommendUser.queryScoreMax(id);
        return user;
    }
}
