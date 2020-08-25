package com.qianshou.friend;

import com.qianshou.friend.dao.FriendDao;
import com.qianshou.friend.pojo.Users;
import com.sun.org.apache.xpath.internal.operations.Bool;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FriendService {

    @Autowired
    private FriendDao friendDao;

//    public List<FriendsVo> getFriendRequest(Long userId) {
//    }

    public Boolean addFriend(Long userId, Long friendId) {
        Users friend = this.friendDao.isFriend(userId, friendId);
        if(friend==null){
          return   friendDao.addFriend(userId, friendId);
        }
        if(!friend.getIsFriend()){
           return friendDao.consentFriendRequest(userId, friendId);
        }
        return null ;
    }

    public Boolean consentFriendRequest(Long userId, Long fromId) {
        Boolean aBoolean = friendDao.consentFriendRequest(userId, fromId);
        return aBoolean;
    }


    public List<Users> getAllRequest(Long userId){
       List<Users> friend = friendDao.getAllRequest(userId);
       return friend;
    }
}
