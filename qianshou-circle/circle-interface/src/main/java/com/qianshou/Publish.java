package com.qianshou;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;
import java.util.List;

@Data
@Document("qianshou_circle_publish")
public class Publish implements Serializable {
    @Id
    private ObjectId id;
    private long userId;
    private String text;
    private List<String> medias;
    private Integer seeType;
    private List<Long> seeList;
    private List<Long> noSeeList;

    @JsonIgnore
    private Double longitude;
    @JsonIgnore
    private Double latitude;
    private String city;
    private long create;
}
