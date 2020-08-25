package com.qianshou.recommend.service;

import com.qianshou.recommend.pojo.qianshouUser;

import java.util.List;

public interface RecommendUser {
    qianshouUser queryScoreMax(Long userId);

    List<qianshouUser> queryPageInfo();
}
