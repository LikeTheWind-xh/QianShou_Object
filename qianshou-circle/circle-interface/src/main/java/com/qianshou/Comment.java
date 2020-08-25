package com.qianshou;

import lombok.Data;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;

@Data
@Document("qianshou_circle_comment")
public class Comment implements Serializable {
    @Id
    private ObjectId id;
    private ObjectId publishId; //发布Id;
    private Integer commentType; //评论类型 1点赞 2评论
    private String content; //评论内容
    private Long   userId; //评论人id
    private boolean isParent; //是否为父节点
    private ObjectId parentId; //父节点id
    private Long create;
}
