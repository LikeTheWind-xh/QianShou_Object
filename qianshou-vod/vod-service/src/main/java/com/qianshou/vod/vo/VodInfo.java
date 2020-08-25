package com.qianshou.vod.vo;

import lombok.Data;

@Data
public class VodInfo {
    private String id;
    private String vodUrl;
    private String coverUrl;
    private Long userId;
    private String userName;
    private String userLogo;
    private Long likeCount;
    private Long commentCount;
    private String content;
    private Boolean isLike;
}
