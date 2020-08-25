package com.qianshou.item.client;

import com.qianshou.UploadApi;
import org.springframework.cloud.openfeign.FeignClient;

@FeignClient("upload-service")
public interface UploadClient extends UploadApi {

}
