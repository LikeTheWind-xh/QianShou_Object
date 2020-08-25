package com.qianshou.elasticsearch.service;

import com.qianshou.elasticsearch.dao.ElasticSearchDao;
import com.qianshou.elasticsearch.pojo.ElasticsearchUser;
import org.elasticsearch.common.geo.GeoDistance;
import org.elasticsearch.common.unit.DistanceUnit;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.GeoDistanceQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.sort.GeoDistanceSortBuilder;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

@Service
public class EsService {

    @Autowired
    private ElasticSearchDao elasticSearchDao;

    public int saveUser(ElasticsearchUser user){
        ElasticsearchUser esUser = elasticSearchDao.save(user);
        if(esUser!=null){
            return 1;
        }
        return 0;
    }

    public Page<ElasticsearchUser> getNearUser(Long userId,double longitude, double latitude, String distance , Pageable pageable){

        NativeSearchQueryBuilder nativeSearchQueryBuilder = new NativeSearchQueryBuilder();
//        Integer sex = 0;
        nativeSearchQueryBuilder.withPageable(pageable);
        BoolQueryBuilder boolQueryBuilder = new BoolQueryBuilder();

        GeoDistanceQueryBuilder geoDistanceQueryBuilder = new GeoDistanceQueryBuilder("userLocation");
        geoDistanceQueryBuilder.point(latitude,longitude);
        geoDistanceQueryBuilder.distance(distance, DistanceUnit.KILOMETERS);
        boolQueryBuilder.mustNot(QueryBuilders.termQuery("id",userId));
//        boolQueryBuilder.mustNot(QueryBuilders.termQuery("sex",sex==1?2:1));
        boolQueryBuilder.filter(geoDistanceQueryBuilder);
        nativeSearchQueryBuilder.withQuery(boolQueryBuilder);
        GeoDistanceSortBuilder geoDistanceSortBuilder = new GeoDistanceSortBuilder("userLocation",latitude,longitude);
        geoDistanceSortBuilder.unit(DistanceUnit.KILOMETERS);
        geoDistanceSortBuilder.order(SortOrder.ASC);
        nativeSearchQueryBuilder.withSort(geoDistanceSortBuilder);

        Page<ElasticsearchUser> userMap = elasticSearchDao.search(nativeSearchQueryBuilder.build());

        if(userMap==null){
            return null;
        }

        userMap.forEach(item->{
            double calculate = GeoDistance.ARC.calculate(latitude, longitude, item.getUserLocation().getLat(), item.getUserLocation().getLon(), DistanceUnit.KILOMETERS);
            calculate = (double)Math.round(calculate * 100) / 100;
            item.setDistance(calculate);
        });
        System.out.println(userMap);

        return userMap;

    }
}
