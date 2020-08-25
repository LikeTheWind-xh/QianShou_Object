package com.qianshou.user.handler;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.tomcat.jni.Local;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@MapperScan("com.qianshou.user")
public class MyMetaObjectHandler implements MetaObjectHandler {
    @Override
    public void insertFill(MetaObject metaObject) {
        this.strictInsertFill(metaObject,"created", LocalDateTime.class,LocalDateTime.now());
        this.strictInsertFill(metaObject,"updated",LocalDateTime.class,LocalDateTime.now());
    }

    @Override
    public void updateFill(MetaObject metaObject) {
        this.strictInsertFill(metaObject,"updated",LocalDateTime.class,LocalDateTime.now());
    }
}
