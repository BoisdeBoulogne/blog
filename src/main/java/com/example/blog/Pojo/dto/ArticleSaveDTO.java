package com.example.blog.Pojo.dto;


import lombok.Data;

import java.util.List;

@Data
public class ArticleSaveDTO {

    private String title;

    private String content;

    private List<Long> tagIds;
}
