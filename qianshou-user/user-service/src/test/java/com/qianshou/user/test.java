package com.qianshou.user;

import com.qianshou.pojo.UserInfo;
import com.qianshou.user.service.impl.UserInfoServiceImpl;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class test {

    @Autowired
    private UserInfoServiceImpl userInfoService;

    @Test
    public void testSelect(){
        List<Long> ids = new ArrayList<>();
        ids.add(1L);
        ids.add(2L);
        List<UserInfo> infosByIds = userInfoService.getInfosByIds(ids);
        for (UserInfo userInfo : infosByIds){
            System.out.println(userInfo.toString());
        }
    }
}
