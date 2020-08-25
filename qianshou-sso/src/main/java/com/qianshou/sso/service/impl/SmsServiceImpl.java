package com.qianshou.sso.service.impl;

import com.aliyuncs.dysmsapi.model.v20170525.SendSmsResponse;
import com.aliyuncs.exceptions.ClientException;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import com.qianshou.sso.service.SmsService;
import com.qianshou.sso.sms.Util.SmsUtils;
import com.qianshou.sso.sms.config.SmsProperties;
import utlis.Result;

import java.util.concurrent.TimeUnit;

@Service
public class SmsServiceImpl implements SmsService {
    private final String PREFIX="user:verify:";

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Autowired
    private SmsProperties properties;

    @Autowired
    private SmsUtils smsUtils;

    @Override
    public Result checkCode(String mobile) {
        try {
            String checkCode = redisTemplate.opsForValue().get(PREFIX + mobile);
            if(StringUtils.isNotEmpty(checkCode)){
                return Result.error().message("验证码还没过期，不能重复发送");
            }
            String code = this.sendCode(mobile);
            if(code==null){
                return Result.error().message("发送消息失败");
            }

            redisTemplate.opsForValue().set(PREFIX+mobile,code,5, TimeUnit.HOURS);

            return Result.ok().message("短信发送成功");
        } catch (ClientException e) {
            e.printStackTrace();
            return Result.error().message("短信发送出现异常,请重试");
        }
    }


    @Override
    public String sendCode(String mobile) throws ClientException {
        String code = RandomStringUtils.randomNumeric(6);
        SendSmsResponse response = smsUtils.sendSms(mobile, code, properties.getSignName(), properties.getVerifyCodeTemplate());
        String responseCode = response.getCode();
        System.out.println(responseCode);
        if(!"OK".equals(responseCode)){
            return null;
        }
        return code;
    }
}
