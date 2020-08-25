package com.qianshou.friend.pojo;

import lombok.Builder;
import lombok.Data;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document("qianshou_friends")
@Data
@Builder
public class Users {
    @Id
    private ObjectId id;
    private Long userId;
    private Long friendId;
    private Boolean isFriend;
}
