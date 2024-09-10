package com.example.blog.Pojo.dto;

import lombok.Data;

@Data
public class CommentDTO {
    private String comment;
    private Long articleId;
}
