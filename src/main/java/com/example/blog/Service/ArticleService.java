package com.example.blog.Service;

import com.example.blog.Pojo.Result.PageResult;
import com.example.blog.Pojo.Result.Result;
import com.example.blog.Pojo.dto.ArticleSaveDTO;
import com.example.blog.Pojo.dto.KeyWordDTO;
import com.example.blog.Pojo.dto.SearchArticlesByTagIdDTO;
import com.example.blog.Pojo.entity.Tag;
import com.example.blog.Pojo.vo.ArticleVo;
import com.example.blog.Pojo.vo.ArticleVoForPre;
import com.example.blog.Pojo.vo.HomePageVo;

import java.util.List;

public interface ArticleService {
    Result<ArticleVo> getById(Long id);

    Result<String> save(ArticleSaveDTO articleSaveDTO);

    Result<PageResult> getUsefulById(Integer page);

    Result<String> delete(Long id);





    Result<String> updateWithId(ArticleSaveDTO article, Long id);


    Result<HomePageVo> forHomePage(int pageNum);

    Result<PageResult<ArticleVoForPre>> getByKeyWord(KeyWordDTO keyword);

    Result<PageResult<ArticleVoForPre>> commonTags(SearchArticlesByTagIdDTO dto);

    Result<List<Tag>> getTagsByArticleId(Long articleId);
}
