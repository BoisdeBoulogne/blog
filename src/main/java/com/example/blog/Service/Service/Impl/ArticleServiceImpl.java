package com.example.blog.Service.Service.Impl;

import com.example.blog.Mapper.*;
import com.example.blog.Pojo.Result.PageResult;
import com.example.blog.Pojo.Result.Result;
import com.example.blog.Pojo.dto.ArticleSaveDTO;
import com.example.blog.Pojo.dto.KeyWordDTO;
import com.example.blog.Pojo.entity.Article;
import com.example.blog.Pojo.entity.Tag;
import com.example.blog.Pojo.vo.ArticleVo;
import com.example.blog.Pojo.vo.ArticleVoForPre;
import com.example.blog.Pojo.vo.CommentVo;
import com.example.blog.Service.ArticleService;
import com.example.blog.constants.OtherConstants;
import com.example.blog.utils.ThreadInfo;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    @Autowired
    private HistoryMapper historyMapper;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    LikeMapper likeMapper;
    @Autowired
    CollectMapper collectMapper;
    @Autowired
    CommentMapper commentMapper;
    @Override
    @Transactional //通过事务来确保如果访问失败不会影响浏览数
    public Result<ArticleVo> getById(Long id) {
        Article article = articleMapper.getById(id);//获取文章详情
        article.setViews(article.getViews() + 1);
        if (article == null) {
            return Result.error("不存在的文章");
        }
        ThreadInfo.setThread(10L);
        Long userId = ThreadInfo.getThread();//获取当前用户ID
        if (userId != null) {
            LocalDateTime now = LocalDateTime.now();
            historyMapper.insert(id,userId,now);
        }
        articleMapper.update(article);

        List<Long> tagIds = tag2ArticlesMapper.getByArticleId(id);
        List<Tag> tags = new ArrayList<>();
        for (Long tagId : tagIds) {
            Tag tag = tagMapper.getById(tagId);
            tags.add(tag);
        }

        ArticleVo articleVo = new ArticleVo();
        BeanUtils.copyProperties(article, articleVo);
        articleVo.setTags(tags);
        List<CommentVo> commentVos = commentMapper.getCommentsByArticleId(id);
        articleVo.setComments(commentVos);
        return Result.success(articleVo);
    }

    @Override
    public Result<String> save(ArticleSaveDTO articleSaveDTO) {
        Article article = new Article();
        BeanUtils.copyProperties(articleSaveDTO, article);
        article.setCreateTime(LocalDateTime.now());
        article.setUpdateTime(LocalDateTime.now());
        article.setViews(0);
        Long userId = ThreadInfo.getThread();
        article.setUserId(userId);
        String author = userMapper.getNickNameById(userId);
        article.setAuthor(author);
        articleMapper.save(article);
        Long articleId = article.getId();
        List<Long> tagIds = articleSaveDTO.getTagIds();
        if (tagIds != null && tagIds.size() > 0) {
            for (Long tagId : tagIds) {
                tag2ArticlesMapper.insert(articleId,tagId); //tag和article映射表
            }
        }

        return Result.success();
    }
    @Override
    public Result<PageResult> getUsefulById(Integer pageNum) {
        PageHelper.startPage(pageNum, OtherConstants.pageSize);
        Long userId = ThreadInfo.getThread();
        Page<Article> page = articleMapper.getUsefulById(userId);
        List<Article> articles = page.getResult();
        List<ArticleVo> articleVos = new ArrayList<>();
        for (Article article : articles) {
            ArticleVo articleVo = new ArticleVo();
            BeanUtils.copyProperties(article, articleVo);
            articleVos.add(articleVo);
            List<Long> tagIds = tag2ArticlesMapper.getTagsIdByArticleId(article.getId());
            List<Tag> tags = new ArrayList<>();
            for (Long tagId : tagIds) {
                Tag tag = tagMapper.getById(tagId);
                tags.add(tag);
            }
            ArticleVoForPre articleVoForPre = new ArticleVoForPre();
            BeanUtils.copyProperties(article, articleVoForPre);
            articleVoForPre.setTags(tags);
        }
        PageResult<ArticleVo> pageResult = new PageResult<ArticleVo>();
        pageResult.setTotal(articles.size());
        pageResult.setList(articleVos);
        return Result.success(pageResult);
    }

    @Override
    public void delete(Long id) {
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

    @Override
    public void updateWithId(ArticleSaveDTO article, Long id) {
        Article article1 = articleMapper.getById(id);
        article1.setUpdateTime(LocalDateTime.now());
        article1.setContent(article.getContent());
        article1.setTitle(article.getTitle());//三个更新字段
        List<Long> tags = article.getTagIds();
        if (tags != null) {
            tag2ArticlesMapper.deleteByArticleId(id);
            for (Long tagId : tags) {
                tag2ArticlesMapper.insert(id,tagId);
            }
        }

        BeanUtils.copyProperties(article, article1);
        articleMapper.update(article1);
    }

    @Override
    public List<ArticleVoForPre> forHomePage() {
        PageHelper.startPage(1,OtherConstants.pageSize);
        List<Long> articlesId = articleMapper.forHomePage();
        List<ArticleVoForPre> articleVos = new ArrayList<>();
        for (Long articleId : articlesId) {
            Article article = articleMapper.getById(articleId);
            ArticleVoForPre articleVoForPre = new ArticleVoForPre();
            BeanUtils.copyProperties(article, articleVoForPre);
            List<Long> tags = tag2ArticlesMapper.getTagsIdByArticleId(articleId);
            List<Tag> tagList = new ArrayList<>();
            for (Long tagId : tags) {
                Tag tag = tagMapper.getById(tagId);
                tagList.add(tag);
            }
            articleVoForPre.setTags(tagList);
            articleVos.add(articleVoForPre);
        }
        return articleVos;
    }

    @Override
    public Result<PageResult<ArticleVoForPre>> getByKeyWord(KeyWordDTO keyword) {
        PageHelper.startPage(keyword.getPageNum(),OtherConstants.pageSize);
        List<Long> articlesId = articleMapper.getIdsByKeyWord(keyword.getKeyword());
        if (articlesId == null || articlesId.size() == 0) {
            return Result.error("不存在");
        }
        List<ArticleVoForPre> articleVos = new ArrayList<>();
        for (Long articleId : articlesId) {
            Article article = articleMapper.getById(articleId);
            ArticleVoForPre articleVoForPre = new ArticleVoForPre();
            BeanUtils.copyProperties(article, articleVoForPre);
            List<Long> tags = tag2ArticlesMapper.getTagsIdByArticleId(articleId);
            List<Tag> tagList = new ArrayList<>();
            for (Long tagId : tags) {
                Tag tag = tagMapper.getById(tagId);
                tagList.add(tag);
            }
            articleVoForPre.setTags(tagList);
            articleVos.add(articleVoForPre);
        }
        PageResult pageResult = new PageResult();
        pageResult.setTotal(articlesId.size());
        pageResult.setList(articleVos);
        return Result.success(pageResult);
    }
}
