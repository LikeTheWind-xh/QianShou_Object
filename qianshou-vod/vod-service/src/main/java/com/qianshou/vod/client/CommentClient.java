package com.qianshou.vod.client;

import com.qianshou.api.CommentApi;
import org.springframework.cloud.openfeign.FeignClient;

@FeignClient("circle-service")
public interface CommentClient extends CommentApi {
}
