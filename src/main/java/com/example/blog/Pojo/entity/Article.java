package com.example.blog.Pojo.entity;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class Article {
    private Long id;
    private String title;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
    private Integer views;
    private Long userId;
    private String content;
    private String author;
}
