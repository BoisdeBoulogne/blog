package com.example.blog.Pojo.vo;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class ArticleVo {
    private String title;
    private String content;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
    private String author;
    private List<String> tagsName;
    private Integer views;
    private Integer likes;
    private Integer collects;
    private List<CommentVo> comments;
}
