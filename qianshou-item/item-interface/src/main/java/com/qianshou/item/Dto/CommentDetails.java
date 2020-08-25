package com.qianshou.item.Dto;

import com.qianshou.Comment;
import lombok.Data;

import java.util.List;

@Data
public class CommentDetails {
    private Long userId;
    private String userName;
    private String userLogo;
    private Comment comments;
}
