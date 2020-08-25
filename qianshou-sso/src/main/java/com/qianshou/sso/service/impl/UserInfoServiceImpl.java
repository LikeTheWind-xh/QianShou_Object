package com.qianshou.sso.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import org.springframework.stereotype.Service;
import com.qianshou.sso.mapper.UserInfoMapper;
import com.qianshou.sso.pojo.UserInfo;
import com.qianshou.sso.service.UserInfoService;

/**
 * <p>
 * 用户信息表 服务实现类
 * </p>
 *
 * @author xiehao
 * @since 2020-07-02
 */
@Service
public class UserInfoServiceImpl extends ServiceImpl<UserInfoMapper, UserInfo> implements UserInfoService {

}
