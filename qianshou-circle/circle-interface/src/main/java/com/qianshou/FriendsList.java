package com.qianshou;


import lombok.Data;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
public class FriendsList {
    @Id
    private ObjectId id;

    //好友id
    private Long userId;

    //发布Id
    private ObjectId publishId;
    private long create;
}
