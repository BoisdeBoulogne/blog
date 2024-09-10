package com.example.blog.Service;

import com.example.blog.Pojo.Result.PageResult;
import com.example.blog.Pojo.Result.Result;
import com.example.blog.Pojo.dto.ArticleSaveDTO;
import com.example.blog.Pojo.dto.KeyWordDTO;
import com.example.blog.Pojo.entity.Article;
import com.example.blog.Pojo.entity.Tag;
import com.example.blog.Pojo.vo.ArticleVo;
import com.example.blog.Pojo.vo.ArticleVoForPre;

import java.util.List;

public interface ArticleService {
    Result<ArticleVo> getById(Long id);

    Result<String> save(ArticleSaveDTO articleSaveDTO);

    Result<PageResult> getUsefulById(Integer page);

    void delete(Long id);

    void update(Article article);

    List<Tag> getArticleTags(Long articleId);

    void updateWithId(ArticleSaveDTO article, Long id);


    List<ArticleVoForPre> forHomePage();

    PageResult<ArticleVoForPre> getByKeyWord(KeyWordDTO keyword);
}
