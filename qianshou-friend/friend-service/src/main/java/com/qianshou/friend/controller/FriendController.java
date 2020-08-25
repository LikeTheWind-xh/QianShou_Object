package com.qianshou.friend.controller;

import com.qianshou.friend.FriendService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import utlis.Result;

@RestController
public class FriendController {
    @Autowired
    private FriendService friendService;

    @PostMapping("addFriend")
    public Result addFriend(@RequestHeader("userId") Long fromId, Long toId){
        friendService.addFriend(toId,fromId);
        return null;
    }

    @PostMapping("getAllRequest")
    @RequestMapping(method = RequestMethod.POST)
    public Result getFriendRequest(@RequestHeader("userId") Long userId){
//       List<FriendsVo> list = friendService.getFriendRequest(userId);
       return null;
    }

    @PostMapping("consentFriendRequest/{dromId}")
    public Result consentFriendRequest(@RequestHeader("userId") Long userId,@PathVariable("fromId") Long RequestId){
        Boolean flag = friendService.consentFriendRequest(userId,RequestId);
        return null;
    }
}
