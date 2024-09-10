package com.example.blog.Pojo.entity;

import lombok.Data;

@Data
public class Comments {
    private String comment;
    private String userNickname;
    private Long userId;
    private Long articleId;
}
