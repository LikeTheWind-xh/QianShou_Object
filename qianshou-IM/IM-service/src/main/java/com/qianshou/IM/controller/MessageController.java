package com.qianshou.IM.controller;

import com.qianshou.IM.service.MessageService;
import com.qianshou.IM.vo.MessageVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import utlis.Result;

import java.util.List;

@RestController
public class MessageController {
    @Autowired
    private MessageService messageService;
    @GetMapping("/getMessage")
    public Result findMessageByToIdAndFromId(Long fromId, Long toId, @RequestParam(defaultValue = "1") Integer page, @RequestParam(defaultValue = "6")Integer pageSize){
        if(fromId==null&&toId==null){
            return Result.error().message("滚 草拟马");
        }
        List<MessageVo> message = messageService.findMessageByToIdAndFromId(fromId, toId, page, pageSize);
        return Result.ok().data("message",message);
    }

    @GetMapping("/getUserMsgList")
    public List<MessageVo> getUserMsgList(Long userId){
        List<MessageVo> list = messageService.getUserMessageList(userId);
        return list;
    }
}
