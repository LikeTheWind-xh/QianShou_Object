package com.qianshou.user.controller;


import com.aliyuncs.exceptions.ClientException;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.qianshou.pojo.User;
import com.qianshou.user.service.UserService;
import com.qianshou.user.service.impl.SmsService;
import com.qianshou.user.service.impl.UserServiceImpl;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import utlis.Result;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * <p>
 * 用户表 前端控制器
 * </p>
 *
 * @author xiehao
 * @since 2020-07-10
 */
@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserServiceImpl userService;

    @Autowired
    private SmsService smsService;

    @PostMapping("/login")
    public Result login(@RequestParam("mobile") String mobile,@RequestParam("password") String password){
        if(mobile==null||password==null){
            return Result.error().message("用户名或密码不能为空");
        }
        Result result = userService.login(mobile, password);
        return result;
    }

    @PostMapping("sendCode")
    public Result sendCode(String mobile) throws ClientException {
        if(StringUtils.isEmpty(mobile)){
            return Result.error().message("手机号不能为空");
        }
        Pattern p = Pattern.compile("^((13[0-9])|(15[^4,\\D])|(18[0,5-9]))\\d{8}$");
        Matcher m = p.matcher(mobile);
        boolean flag = m.matches();
        if(!flag){
            return Result.error().message("手机号格式有误");
        }
        Result result = smsService.verifyCode(mobile);
        return result;

    }

    @PostMapping("/register")
    public Result register(@Validated User user, @RequestParam("verificationCode") String code){
        Result result = this.userService.register(user, code);
        return result;
    }

}

