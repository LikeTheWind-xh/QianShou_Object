package com.qianshou.user.controller;


import com.qianshou.pojo.UserInfo;
import com.qianshou.user.service.UserInfoService;
import com.qianshou.user.service.impl.UserInfoServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import utlis.Result;

import java.util.List;

/**
 * <p>
 * 用户信息表 前端控制器
 * </p>
 *
 * @author xiehao
 * @since 2020-07-10
 */
@RestController
@Slf4j
@RequestMapping("/userInfo")
public class UserInfoController {

    @Autowired
    private UserInfoServiceImpl userInfoService;

    @GetMapping("/getUserInfo")
    public Result findInfoById(@RequestHeader("userId") Long userId) {
        if (userId == null) {
            return Result.error().message("用户未登录");
        }
        Result result = userInfoService.getUserInfo(userId);
        return result;
    }

    @PostMapping("/saveUser")
    public Result saveUser(UserInfo userInfo,
                           @RequestHeader("Authorization") String token,
                           @RequestParam(value = "longitude") double longitude,
                           @RequestParam(value = "latitude") double latitude) {
        if (userInfo == null) {
            return null;
        }
        log.info("经度：{},维度：{}",longitude,latitude);
        Result result = userInfoService.saveUser(userInfo, token ,longitude,latitude);
        return result;
    }

    @GetMapping("findUserInfos")
    public List<UserInfo> findUserInfos(@RequestParam("ids") List<Long> ids){
        List<UserInfo> users = userInfoService.getInfosByIds(ids);
        return users;
    }
}

