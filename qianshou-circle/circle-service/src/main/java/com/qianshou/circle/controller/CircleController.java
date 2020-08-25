package com.qianshou.circle.controller;

import com.qianshou.circle.vo.PublishInfo;
import com.qianshou.Publish;
import com.qianshou.circle.service.CircleService;
import io.swagger.annotations.ApiModelProperty;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import utlis.Result;

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
    public Boolean createPublish(Publish publish, @RequestParam("userId") Long id) {
        if (publish == null || id == null) {
            return null;
        }
        publish.setUserId(id);
        Boolean b = circleService.savePublish(publish);
        return b;
    }

    @ApiModelProperty("获取动态详情")
    @GetMapping("publish/detailPage/{id}")
    public Result getPublishDetailPage(@PathVariable("id") String publishId){
        PublishInfo detailPage = circleService.getDetailPage(publishId);
        return Result.ok().data("detailPage",detailPage);
    };

    @GetMapping("publish/getMe")
    public Result getMeAll(@RequestParam Long userId){
        Result meAll = circleService.getMeAll(userId);
        return meAll;
    }

}
