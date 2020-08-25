package com.qianshou.recommend.pojo;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document("qianshou_recommend")
public class qianshouUser {

    @Id
    private ObjectId id;

    @Indexed
    private Long userId;

    private Long toId;

    @Indexed
    private Double score;

    private String date;

}
