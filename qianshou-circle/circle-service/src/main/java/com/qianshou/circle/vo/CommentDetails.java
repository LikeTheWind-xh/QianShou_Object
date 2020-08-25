package com.qianshou.circle.vo;

import com.qianshou.Comment;
import lombok.Data;

@Data
public class CommentDetails {
    private Long userId;
    private String userName;
    private String userLogo;
    private Comment comments;
}
