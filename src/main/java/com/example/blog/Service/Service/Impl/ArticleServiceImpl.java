package com.example.blog.Service.Service.Impl;


import com.example.blog.Mapper.*;
import com.example.blog.Pojo.Result.PageResult;
import com.example.blog.Pojo.Result.Result;
import com.example.blog.Pojo.dto.ArticleSaveDTO;
import com.example.blog.Pojo.dto.KeyWordDTO;
import com.example.blog.Pojo.dto.SearchArticlesByTagIdDTO;
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
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;


import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


@Service
@Slf4j
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
    @Autowired
    StringRedisTemplate stringRedisTemplate;
    @Override
    public Result<ArticleVo> getById(Long id) {
        Long userId = ThreadInfo.getThread();

        Article article = articleMapper.getById(id);
        if (article == null) {
            return Result.error("不存在的文章");
        }
        if (userId != null) {
            historyMapper.insert(id,userId,LocalDateTime.now());
        }

        articleMapper.addViews(id);
        ArticleVo articleVo = new ArticleVo();
        BeanUtils.copyProperties(article, articleVo);
        List<Long> tagIds = tag2ArticlesMapper.getTagsIdByArticleId(id);
        List<Tag> tags = new ArrayList<>();
        for (Long tagId : tagIds) {
            Tag tag = tagMapper.getById(tagId);
            tags.add(tag);
        }
        articleVo.setTags(tags);
        List<CommentVo> commentVos = commentMapper.getCommentsByArticleId(id);
        articleVo.setComments(commentVos);
        return Result.success(articleVo);
    }


    @Override
    public Result<String> save(ArticleSaveDTO articleSaveDTO) {
        List<Long> tagIds = articleSaveDTO.getTagIds();

        if (tagIds != null && tagIds.size() > 0) {
            for (Long tagId : tagIds) {
                Tag tag = tagMapper.getById(tagId);
                if (tag == null) {
                    return Result.error("不存在的标签");
                }
            }

        }
        Long articleId = saveA(articleSaveDTO);
        if (tagIds != null) {
            for (Long tagId : tagIds) {
                tag2ArticlesMapper.insert(articleId, tagId);
            }
        }
        return Result.success();
    }
    private Long saveA(ArticleSaveDTO articleSaveDTO){
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
            return article.getId();
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
    public Result<String> delete(Long id) {
        Long userId = ThreadInfo.getThread();

        Long realUserId = articleMapper.getUserIdByArticleId(id);
        log.info("userId:{},realUserId:{}", userId, realUserId);
        if (userId == null || (!userId .equals(realUserId))){
            return Result.error("没权限！");
        }
        String cacheKey = "cache:article:" + id;
        articleMapper.deleteById(id);
        stringRedisTemplate.delete(cacheKey);
        return Result.success();
    }

    @Override
    public void update(Article article) {
        article.setUpdateTime(LocalDateTime.now());
        articleMapper.update(article);
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
        String cacheKey = "cache:article:" + id;
        stringRedisTemplate.delete(cacheKey);
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

    @Override
    public Result<PageResult<ArticleVoForPre>> commonTags(SearchArticlesByTagIdDTO dto) {
        PageHelper.startPage(dto.getPageNum(),OtherConstants.pageSize);
        List<Long> articlesId = tag2ArticlesMapper.getArticlesIdByTagId(dto.getTagId());
        Tag tag1 = tagMapper.getById(dto.getTagId());
        if (tag1 == null){
            return Result.error("不存在的标签");
        }
        if (articlesId == null || articlesId.size() == 0) {
            return Result.error("还不存在相关文章");
        }
        List<ArticleVoForPre> articleVoForPres = new ArrayList<>();
        for (Long articleId : articlesId){
            ArticleVoForPre articleVoForPre = new ArticleVoForPre();
            Article article = articleMapper.getById(articleId);
            BeanUtils.copyProperties(article, articleVoForPre);
            List<Long> tags = tag2ArticlesMapper.getTagsIdByArticleId(articleId);
            List<Tag> tagList = new ArrayList<>();
            for (Long tagId : tags) {
                Tag tag = tagMapper.getById(tagId);
                tagList.add(tag);

            }
            articleVoForPre.setTags(tagList);
            articleVoForPres.add(articleVoForPre);
        }
        PageResult pageResult = new PageResult();
        pageResult.setTotal(articlesId.size());
        pageResult.setList(articleVoForPres);
        return Result.success(pageResult);
    }

    @Override
    public Result<List<Tag>> getTagsByArticleId(Long articleId) {
        List<Long> tagsId = tag2ArticlesMapper.getTagsIdByArticleId(articleId);
        if (tagsId == null || tagsId.size() == 0) {
            return Result.error("这篇文章没有标签或者查询文章不存在");
        }
        List<Tag> tagList = new ArrayList<>();
        for (Long tagId : tagsId) {
            Tag tag = tagMapper.getById(tagId);
            tagList.add(tag);
        }
        return Result.success(tagList);
    }
}
