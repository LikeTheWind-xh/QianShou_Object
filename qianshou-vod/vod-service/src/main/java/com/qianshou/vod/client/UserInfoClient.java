package com.qianshou.vod.client;


import com.qianshou.api.UserInfoApi;
import org.springframework.cloud.openfeign.FeignClient;

@FeignClient("user-service")
public interface UserInfoClient extends UserInfoApi {
}
