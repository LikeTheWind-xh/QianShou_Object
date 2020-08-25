package com.qianshou.elasticsearch.dao;

import com.qianshou.elasticsearch.pojo.ElasticsearchUser;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ElasticSearchDao extends ElasticsearchRepository<ElasticsearchUser,Long> {

}
