package com.qianshou.elasticsearch.controller;

import com.qianshou.elasticsearch.pojo.ElasticsearchUser;
import com.qianshou.elasticsearch.service.EsService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.core.geo.GeoPoint;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Api(tags = "es用户操作")
@RestController
@CrossOrigin
public class UserController {

    @Autowired
    private EsService esService;

    @ApiOperation("获取附近好友")
    @GetMapping("/getNearUser")
    public Page<ElasticsearchUser> getNearUser(Long userId,double longitude,double latitude , String distance ,Integer page , Integer size){
        PageRequest pageRequest = PageRequest.of(page-1, size);
        Page<ElasticsearchUser> nearUser = esService.getNearUser(userId,longitude, latitude, distance, pageRequest);

        return nearUser;
    }

    @ApiOperation("添加用户")
    @PostMapping("/addUser")
    public Integer saveUser(@RequestBody ElasticsearchUser elasticsearchUser, double longitude, double latitude){
        System.out.println(elasticsearchUser);
        GeoPoint geoPoint = new GeoPoint(latitude,longitude);
        elasticsearchUser.setUserLocation(geoPoint);
        if(elasticsearchUser==null){
            return null;
        }
        int flag = esService.saveUser(elasticsearchUser);
        return flag;
    }
}
