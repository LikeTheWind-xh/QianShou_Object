package com.qianshou.user.client;


import com.qianshou.elasticsearch.client.EsApi;
import org.springframework.cloud.openfeign.FeignClient;

@FeignClient("elasticsearch-service")
public interface EsClient extends EsApi {
}
