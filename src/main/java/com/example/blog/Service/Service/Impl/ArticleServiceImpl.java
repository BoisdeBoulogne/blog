package com.example.blog.Service.Service.Impl;

import com.example.blog.Mapper.ArticleMapper;
import com.example.blog.Mapper.Tag2ArticlesMapper;
import com.example.blog.Mapper.TagMapper;
import com.example.blog.Pojo.Result.PageResult;
import com.example.blog.Pojo.Result.Result;
import com.example.blog.Pojo.dto.ArticleSaveDTO;
import com.example.blog.Pojo.entity.Article;
import com.example.blog.Pojo.entity.Tag;
import com.example.blog.Pojo.vo.ArticleVo;
import com.example.blog.Service.ArticleService;
import com.example.blog.constants.OtherConstants;
import com.example.blog.utils.ThreadInfo;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class ArticleServiceImpl implements ArticleService {
    @Autowired
    private ArticleMapper articleMapper;
    @Autowired
    private TagMapper tagMapper;
    @Autowired
    private Tag2ArticlesMapper tag2ArticlesMapper;
    @Override
    public Result<Article> getById(Integer id) {
        Article article = articleMapper.getById(id);
        if (article == null) {
            return Result.error("不存在的文章");
        }
        return Result.success(article);
    }

    @Override
    public Result<String> save(ArticleSaveDTO articleSaveDTO) {
        Article article = new Article();
        BeanUtils.copyProperties(articleSaveDTO, article);
        article.setCreateTime(LocalDateTime.now());
        article.setUpdateTime(LocalDateTime.now());
        article.setViews(0);
        //TODO 后续使用LocalThread
        article.setUserId(10L);
        articleMapper.save(article);
        Long articleId = article.getId();
        Long tagId = articleSaveDTO.getTagId();
        tag2ArticlesMapper.insert(articleId,tagId);
        return Result.success();
    }

    @Override
    public Result<PageResult> getUsefulById(Integer pageNum) {
        PageHelper.startPage(pageNum, OtherConstants.pageSize);
        // todo threadlocal
        Long userId = 1L;
        Page<ArticleVo> page = articleMapper.getUsefulById(userId);
        PageResult pageResult = new PageResult();
        pageResult.setTotal(page.getPages());
        pageResult.setList(page.getResult());
        return Result.success(pageResult);
    }

    @Override
    public void delete(Integer id) {
        articleMapper.deleteById(id);
    }

    @Override
    public void update(Article article) {
        article.setUpdateTime(LocalDateTime.now());
        articleMapper.update(article);
    }

    @Override
    public List<Tag> getArticleTags(Long articleId) {
        List<Long> tags = tag2ArticlesMapper.getTagsIdByArticleId(articleId);
        List<Tag> tagList = new ArrayList<>();
        for (Long tagId : tags) {
            Tag tag = tagMapper.getById(tagId);
            tagList.add(tag);
        }
        return tagList;
    }
}
