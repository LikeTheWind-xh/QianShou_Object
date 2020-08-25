package com.qianshou.recommend.clinet;

import com.qianshou.recommend.pojo.qianshouUser;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

public interface RecommendApi {
    @GetMapping("/getRecommend")
    qianshouUser getRecommendById(@RequestParam("id") Long id);
}
