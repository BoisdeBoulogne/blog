package com.example.blog.Controller;

import com.example.blog.Pojo.Result.PageResult;
import com.example.blog.Pojo.Result.Result;
import com.example.blog.Pojo.dto.ArticleSaveDTO;
import com.example.blog.Pojo.dto.SearchArticlesByTagIdDTO;
import com.example.blog.Pojo.entity.Article;
import com.example.blog.Pojo.entity.Tag;
import com.example.blog.Pojo.vo.ArticleVo;
import com.example.blog.Pojo.vo.ArticleVoForPre;
import com.example.blog.Service.ArticleService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.annotations.Delete;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.web.ServerProperties;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Slf4j
@RequestMapping("/article")
public class ArticleController {
    @Autowired
    private ArticleService articleService;

    @GetMapping("/{id}")
    public Result<ArticleVo> getArticle(@PathVariable Long id) {
        return articleService.getById(id);
    }
    @PostMapping("/save")
    public Result<String> save(@Valid @RequestBody ArticleSaveDTO articleSaveDTO) {
        return articleService.save(articleSaveDTO);
    }

    @GetMapping("/deleteOrUpdatePage/{page}")
    public Result<PageResult> deleteOrUpdatePage(@PathVariable Integer page) {
        return articleService.getUsefulById(page);
    }

    @DeleteMapping("/delete/{id}")
    public Result<String> delete(@PathVariable Long id) {
        return  articleService.delete(id);
    }

    @PutMapping("/update/{id}")
    public Result<String> update(@RequestBody ArticleSaveDTO article, @PathVariable Long id) {

        articleService.updateWithId(article,id);
        return Result.success();
    }

    @GetMapping("/commonTags")
    public Result<PageResult<ArticleVoForPre>> commonTags(@RequestBody SearchArticlesByTagIdDTO dto) {
        log.info(dto.toString());
        return articleService.commonTags(dto);

    }

    @GetMapping("/tag/{articleId}")
    public Result<List<Tag>> tag(@PathVariable Long articleId) {
        return articleService.getTagsByArticleId(articleId);
    }

}