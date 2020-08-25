package com.qianshou.elasticsearch.pojo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;
import org.springframework.data.elasticsearch.annotations.GeoPointField;
import org.springframework.data.elasticsearch.core.geo.GeoPoint;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(value = "ElasticsearchUser对象",description = "用户表")
@Document(indexName = "qianshou",type = "user",shards = 1,replicas = 0)
public class ElasticsearchUser {
    @Id
    @ApiModelProperty("主键id")
    private Long id;

    @ApiModelProperty("用户id")
    @Field(type = FieldType.Long)
    private Long userId;

    @ApiModelProperty("婚姻状态")
    @Field(type = FieldType.Keyword)
    private String marriage;

    @ApiModelProperty("所在城市")
    @Field(type = FieldType.Keyword)
    private String city;

    @ApiModelProperty("用户名字")
    @Field(type = FieldType.Text,analyzer = "ik_max_word",index = false)
    private String userName;

    @ApiModelProperty("用户头像")
    @Field(type = FieldType.Keyword,index = false)
    private String userLogo;

    @Field(type = FieldType.Keyword,index = false)
    @ApiModelProperty("封面图片")
    private String coverPic;

    @ApiModelProperty("性别 1男 2女")
    @Field(type = FieldType.Integer)
    private Integer sex;

    @Field(type = FieldType.Text,index = false)
    private String birthday;

    @Field(type = FieldType.Keyword)
    private String edu;

    @GeoPointField
    private GeoPoint userLocation;

    @ApiModelProperty("距离")
    @Transient
    private Double distance;


}