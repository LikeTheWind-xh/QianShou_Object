package com.qianshou.user.service.impl;


import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.qianshou.elasticsearch.pojo.ElasticsearchUser;
import com.qianshou.pojo.UserInfo;
import com.qianshou.user.client.EsClient;
import com.qianshou.user.mapper.UserInfoMapper;
import com.qianshou.user.service.UserInfoService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import utlis.Result;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 用户信息表 服务实现类
 * </p>
 *
 * @author xiehao
 * @since 2020-07-10
 */
@Service
public class UserInfoServiceImpl extends ServiceImpl<UserInfoMapper, UserInfo> implements UserInfoService {

    @Autowired
    private UserInfoMapper userInfoMapper;

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Autowired
    private EsClient esClient;

    public Result getUserInfo(Long userId) {
//        String verifyUser = redisTemplate.opsForValue().get("TOKEN_" + token);
//        if (verifyUser == null) {
//            return Result.error().message("无效的token");
//        }
//        User user = JSON.parseObject(verifyUser, User.class);
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("user_id", userId);
        UserInfo userInfo = userInfoMapper.selectOne(queryWrapper);
        if (userInfo == null) {
            return Result.ok().data("isNew",true);
        }
        return Result.ok().data("userInfo", userInfo);
    }

    public Result saveUser(UserInfo userInfo, String token, double longitude, double latitude) {
        String userToken = redisTemplate.opsForValue().get("TOKEN_" + token);
        Map map = JSON.parseObject(userToken, Map.class);
        String strId = map.get("id").toString();
        Long id = Long.valueOf(strId);
        userInfo.setUserId(id);
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                ElasticsearchUser elasticsearchUser = ElasticsearchUser.builder()
                        .birthday(userInfo.getBirthday())
                        .city(userInfo.getCity())
                        .coverPic(userInfo.getCoverPic())
                        .userLogo(userInfo.getLogo())
                        .userId(userInfo.getUserId())
                        .id(userInfo.getUserId())
                        .userName(userInfo.getNickName())
                        .edu(userInfo.getEdu())
                        .sex(userInfo.getSex())
                        .marriage(userInfo.getMarriage())
                        .build();
                esClient.saveUser(elasticsearchUser, longitude, latitude);
            }
        };
        Thread thread = new Thread(runnable);
        int insert = userInfoMapper.insert(userInfo);
        if(insert>0){
            thread.start();
           return Result.ok();
        }
        return null;

    }

    public List<UserInfo> getInfosByIds(List<Long> ids) {
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.in("user_id",ids);
        List<UserInfo> list = userInfoMapper.selectList(queryWrapper);
        return list;
    }

    public class SaveEs implements Runnable{

        @Override
        public void run() {

        }
    }
}
