package com.qianshou.api;

import com.qianshou.pojo.UserInfo;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import utlis.Result;

import java.util.List;

public interface UserInfoApi {
    @GetMapping("/userInfo/getUserInfo")
     Result findInfoById(@RequestHeader("userId") Long userId);

    @GetMapping("/userInfo/findUserInfos")
    List<UserInfo> findUserInfos(@RequestParam("ids") List<Long> ids);
}
