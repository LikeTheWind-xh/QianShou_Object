package com.qianshou.sso.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import com.qianshou.sso.mapper.UserMapper;
import com.qianshou.sso.pojo.User;
import com.qianshou.sso.service.UserService;
import utlis.Result;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * <p>
 * 用户表 服务实现类
 * </p>
 *
 * @author xiehao
 * @since 2020-07-02
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private StringRedisTemplate redisTemplate;

    private final String PREFIX="user:verify:";

    @Value("${qianshou.secret}")
    private String secret;

    @Override
    public Result register(String mobile,String password, String code) {
        String checkCode = redisTemplate.opsForValue().get(PREFIX + mobile);
        if(!StringUtils.equals(checkCode,code)){
           return Result.error().message("验证码有误");
        }
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("mobile",mobile);
        User user = userMapper.selectOne(queryWrapper);

        if(user!=null){
            return Result.error().message("用户已存在");
        }

        user.setMobile(mobile);
        user.setPassword(password);
        userMapper.insert(user);
        return Result.ok().message("注册成功");
    }



    @Override
    public Result login(String mobile, String password) {
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("mobile",mobile);
        User user = userMapper.selectOne(queryWrapper);
        if(user==null){
            return Result.error().message("该用户不存在");
        }
       if(!StringUtils.equals(DigestUtils.md5Hex(password),user.getPassword())){
           return Result.error().message("账户名或密码有误");
       }

        String token = Jwts.builder()
                .setId(UUID.randomUUID().toString().replaceAll("-",""))
                .signWith(SignatureAlgorithm.ES256,secret)
                .claim("mobile",user.getMobile())
                .claim("id",user.getId())
                .compact();

        redisTemplate.opsForValue().set("TOKEN_"+token,user.toString(),1, TimeUnit.HOURS);
        return Result.ok().message("登录成功").data("token",token);
    }


}
