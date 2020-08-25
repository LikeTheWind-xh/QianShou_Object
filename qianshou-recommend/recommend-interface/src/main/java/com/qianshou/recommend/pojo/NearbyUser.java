package com.qianshou.recommend.pojo;


import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;
import org.springframework.data.elasticsearch.annotations.GeoPointField;
import sun.rmi.runtime.Log;

@Document(indexName = "qianshou",type = "user",shards = 1,replicas = 0)
public class NearbyUser {
    @Id
    private Long id;

    @Field(type = FieldType.Long)
    private Long userId;

    @Field(type = FieldType.Text,analyzer = "ik_max_word")
    private String userName;

    @Field(type = FieldType.Keyword )
    private String userLogo;

    @GeoPointField
    private GeoPointField userLocation;



}
