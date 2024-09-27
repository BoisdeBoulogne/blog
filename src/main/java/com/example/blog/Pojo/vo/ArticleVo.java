package com.example.blog.Pojo.vo;

import com.example.blog.Pojo.entity.Tag;
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
    private Long authorId;
    private List<Tag> tags;
    private Integer views;
    private Integer likes;
    private Integer collects;
    private List<CommentVo> comments;
    private Integer liked;
    private Integer collected;
    private Integer followed;
}
