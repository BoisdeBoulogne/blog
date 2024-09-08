package com.example.blog.Pojo.entity;

import lombok.Data;

@Data
public class Follow {
    private Long id;
    private Long followerId;
    private Long leaderId;
}
