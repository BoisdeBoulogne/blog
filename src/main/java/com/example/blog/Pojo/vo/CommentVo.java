package com.example.blog.Pojo.vo;

import lombok.Data;

@Data
public class CommentVo {
    private String comment;
    private String userNickname;
    private Long userId;
}
