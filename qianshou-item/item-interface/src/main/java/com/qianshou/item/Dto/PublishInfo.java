package com.qianshou.item.Dto;

import com.qianshou.Publish;
import lombok.Data;

import java.util.List;

@Data
public class PublishInfo <T>{
    private String id;
    private Publish publish;
    private String userNme;
    private String userLogo;
    private Long likeCount;
    private Long commentCount;
    private Boolean isLike;
    private List<CommentDetails> commentDetails; ;
}
