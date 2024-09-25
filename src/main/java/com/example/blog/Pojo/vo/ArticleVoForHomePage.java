package com.example.blog.Pojo.vo;

import com.example.blog.Pojo.entity.Tag;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;
@Data
public class ArticleVoForHomePage {
    private Long id;
    private String title;
    private String author;
    private List<Tag> tags;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
    private Integer views;
    private Integer likes;
    private Integer collects;
    private String content;
}
