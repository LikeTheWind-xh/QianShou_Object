package com.qianshou.user.service.impl;

import com.alibaba.fastjson.JSON;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsResponse;
import com.aliyuncs.exceptions.ClientException;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.qianshou.pojo.User;


import com.qianshou.user.mapper.UserMapper;
import com.qianshou.user.service.UserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.qianshou.user.utils.JwtUtils;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.DigestUtils;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import utlis.Result;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * <p>
 * 用户表 服务实现类
 * </p>
 *
 * @author xiehao
 * @since 2020-07-10
 */
@Service
@Slf4j
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private StringRedisTemplate redisTemplate;

    private final String PREFIX = "user:verify:";

    @Value("${jwt.secret}")
    private String secret;

    /**
     * 根据用户名密码查询用户,auth服务调用
     *
     * @param mobile
     * @param password
     * @return
     */
    public Result login(String mobile, String password) {
        QueryWrapper<User> queryWrapper = new QueryWrapper();
        queryWrapper.eq("mobile", mobile);
        User user = userMapper.selectOne(queryWrapper);
        if (user == null) {
            return Result.error().message("该用户不存在");
        } else if (!StringUtils.equals(user.getPassword(), DigestUtils.md5Hex(password))) {
            return Result.error().message("密码错误");
        }
        log.info("password:{}", DigestUtils.md5Hex(password));
        String token = JwtUtils.generateToken(user, secret);
        Map<String, Object> token_value = new HashMap<>();
        token_value.put("id", user.getId());
        token_value.put("mobile", user.getMobile());
        redisTemplate.opsForValue().set("TOKEN_" + token, JSON.toJSONString(token_value), 5, TimeUnit.HOURS);
        return Result.ok().message("登录成功").data("token", token);
    }


    public Result register(User user, String code) {
        String checkCode = redisTemplate.opsForValue().get(PREFIX + user.getMobile());
        if (!StringUtils.equals(checkCode, code)) {
            return Result.error().message("验证码有误");
        }
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("mobile", user.getMobile());
        User checkUser = userMapper.selectOne(queryWrapper);

        if (checkUser != null) {
            return Result.error().message("用户已存在");
        }
        checkUser.setPassword(DigestUtils.md5Hex(user.getPassword()));
        userMapper.insert(checkUser);

        redisTemplate.delete(PREFIX + user.getMobile());
        return Result.ok().message("注册成功");

    }

}
