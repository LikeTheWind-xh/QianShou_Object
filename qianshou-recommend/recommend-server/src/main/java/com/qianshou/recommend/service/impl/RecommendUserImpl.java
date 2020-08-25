package com.qianshou.recommend.service.impl;

import com.qianshou.recommend.pojo.qianshouUser;
import com.qianshou.recommend.service.RecommendUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class RecommendUserImpl implements RecommendUser {

    @Autowired
    private MongoTemplate mongoTemplate;

    @Override
    public qianshouUser queryScoreMax(Long userId) {
        Criteria criteria = Criteria.where("toId").is(userId);
        Query query = Query.query(criteria).with(Sort.by(Sort.Order.desc("score"))).limit(1);
        qianshouUser recommendUser = mongoTemplate.findOne(query, qianshouUser.class);
        return recommendUser;
    }

    @Override
    public List<qianshouUser> queryPageInfo() {
        return null;
    }
}
