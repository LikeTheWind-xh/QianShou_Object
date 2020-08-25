package com.qianshou.user.utils;

import com.qianshou.pojo.User;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class JwtUtils {
    public static String generateToken(User user,String secret){
        Map<String,Object> claims = new HashMap<>();
        claims.put("id",user.getId());
        claims.put("mobile",user.getMobile());
        String token = Jwts.builder()
                .setId(UUID.randomUUID().toString().replaceAll("-", ""))
                .setClaims(claims)
                .signWith(SignatureAlgorithm.HS256, secret)
                .compact();
        return token;

    }
}
