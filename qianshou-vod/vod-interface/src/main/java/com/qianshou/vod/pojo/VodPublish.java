package com.qianshou.vod.pojo;

import lombok.Data;
import net.minidev.json.annotate.JsonIgnore;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document("qianshou-vodPublish")
public class VodPublish {
    @Id
    private ObjectId id;
    private Long userId;
    private String vodUrl;
    private String text;
    private String coverUrl;
    @JsonIgnore
    private Double longitude;
    @JsonIgnore
    private Double latitude;
    private String city;
    private long create;
}
