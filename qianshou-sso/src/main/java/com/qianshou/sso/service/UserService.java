package com.qianshou.sso.service;

import com.baomidou.mybatisplus.extension.service.IService;

import com.qianshou.sso.pojo.User;
import utlis.Result;

/**
 * <p>
 * 用户表 服务类
 * </p>
 *
 * @author xiehao
 * @since 2020-07-02
 */

public interface UserService extends IService<User> {
    Result register(String mobile,String password, String code);

    Result login(String mobile, String password);
}
