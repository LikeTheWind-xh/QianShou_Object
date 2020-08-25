package com.qianshou;

import lombok.Data;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
@Data
@Document("qianshou_circle_mypublish")
public class MyPublish {
    @Id
    private ObjectId id;
    private ObjectId publishId;
    private long create;
}
