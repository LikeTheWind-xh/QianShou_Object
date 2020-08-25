package com.qianshou.item.controller;

import com.qianshou.Publish;
import com.qianshou.item.Dto.PublishInfo;
import com.qianshou.item.service.CircleService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import utlis.Result;

@Api(tags = "朋友圈")
@RestController
@RequestMapping("/circle")
public class CircleController {

    @Autowired
    private CircleService circleService;

    @ApiModelProperty("获取发布的动态")
    @GetMapping("/getPublish")
    public Result getPublish(Long userId, Integer page, Integer pageSize) {
        if (userId == null || page == null || pageSize == null) {
            return null;
        }
        Result result = this.circleService.getPublish(userId, page, pageSize);
        return result;
    }

    @ApiModelProperty("发布动态")
    @PostMapping("/addPublish")
    public Boolean createPublish(Publish publish, @RequestParam("userId") String id) {
        if (publish == null || id == null || id == "") {
            return null;
        }
        publish.setUserId(Long.parseLong(id));
        Boolean b = circleService.savePublish(publish);
        return b;
    }

    @ApiModelProperty("获取动态详情")
    @GetMapping("publish/detailPage/{id}")
    public Result getPublishDetailPage(@PathVariable("id") String publishId){
        PublishInfo detailPage = circleService.getDetailPage(publishId);
        return Result.ok().data("detailPage",detailPage);
    };

}
