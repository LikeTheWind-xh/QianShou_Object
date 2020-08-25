package com.qianshou.IM.vo;

import lombok.Data;
import org.springframework.data.domain.PageRequest;

@Data
public class MessageVo {
    private String id;
    private Long    userId;
    private String UserName;
    private String UserLogo;
    private Integer status;
    private String content;
    private Long toId;
    private Long fromId;
    private Long create;
}
