package com.qianshou.vod.vo;

import com.qianshou.Comment;
import lombok.Data;

@Data
public class CommentDetails {
    private Long userId;
    private String userName;
    private String userLogo;
   private String content;
   private Long create;
}
