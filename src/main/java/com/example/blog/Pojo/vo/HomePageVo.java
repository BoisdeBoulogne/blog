package com.example.blog.Pojo.vo;

import lombok.Data;

import java.util.List;

@Data
public class HomePageVo {
    private List<ArticleVoForHomePage> list;
    private Integer all;
}
