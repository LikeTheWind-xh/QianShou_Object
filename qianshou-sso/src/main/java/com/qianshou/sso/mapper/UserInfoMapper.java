package com.qianshou.sso.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import com.qianshou.sso.pojo.UserInfo;


/**
 * <p>
 * 用户信息表 Mapper 接口
 * </p>
 *
 * @author xiehao
 * @since 2020-07-02
 */
@Mapper
public interface UserInfoMapper extends BaseMapper<UserInfo> {

}
