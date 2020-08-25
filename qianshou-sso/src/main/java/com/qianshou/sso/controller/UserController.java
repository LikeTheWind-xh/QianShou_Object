package com.qianshou.sso.controller;


import com.aliyuncs.exceptions.ClientException;

import com.qianshou.sso.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.qianshou.sso.pojo.User;
import com.qianshou.sso.service.impl.SmsServiceImpl;
import com.qianshou.sso.service.impl.UserServiceImpl;
import utlis.Result;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * <p>
 * 用户表 前端控制器
 * </p>
 *
 * @author xiehao
 * @since 2020-07-02
 */
@RestController
@RequestMapping("/sso/user")
@CrossOrigin
@Slf4j
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private SmsServiceImpl smsService;

    @PostMapping("sendCode")
    public Result sendCode(String mobile) throws ClientException {
        log.info("mobile：{}",mobile);
        if(StringUtils.isEmpty(mobile)){
            return Result.error().message("手机号不能为空");
        }
        Pattern p = Pattern.compile("^((13[0-9])|(15[^4,\\D])|(18[0,5-9]))\\d{8}$");
        Matcher m = p.matcher(mobile);
        boolean flag = m.matches();
        if(!flag){
            return Result.error().message("手机号格式有误");
        }
        Result result = smsService.checkCode(mobile);
        return result;

    }

    @PostMapping("/register")
    public Result register(String mobile,String password, @RequestParam("verificationCode") String code){
        Result result = this.userService.register(mobile,password,code);
        return result;
    }
}

