package com.qianshou.IM.client;

import com.qianshou.api.UserInfoApi;
import org.springframework.cloud.openfeign.FeignClient;

@FeignClient("user-service")
public interface UserClient  extends UserInfoApi {
}
