package com.example.blog.Service;

import com.example.blog.Pojo.Result.PageResult;
import com.example.blog.Pojo.Result.Result;
import com.example.blog.Pojo.dto.ArticleSaveDTO;
import com.example.blog.Pojo.entity.Article;

public interface ArticleService {
    Result<Article> getById(Integer id);

    Result<String> save(ArticleSaveDTO articleSaveDTO);

    Result<PageResult> getUsefulById(Integer page);
}
