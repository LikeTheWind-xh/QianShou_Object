package com.qianshou.pojo;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.Date;

@Data
@Document(collection = "message")
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Message {
    @Id
    private ObjectId id;

    private String msg;

    /**
     * 消息读取状态 1未读 2已读
     */
    @Indexed
    private Integer status;

    @Indexed
    @Field("send_date")
    private Long sendDate;

    @Field("read_date")
    private Long readDate;

    private Long toUserId;

    private Long fromUserId;
}
