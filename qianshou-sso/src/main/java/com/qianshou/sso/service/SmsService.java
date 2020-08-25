package com.qianshou.sso.service;

import com.aliyuncs.exceptions.ClientException;
import utlis.Result;


public interface SmsService {

    Result checkCode(String mobile);
    String sendCode(String mobile) throws ClientException;
}
