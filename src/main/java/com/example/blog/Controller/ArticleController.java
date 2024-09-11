package com.example.blog.Controller;

import com.example.blog.Pojo.Result.PageResult;
import com.example.blog.Pojo.Result.Result;
import com.example.blog.Pojo.dto.ArticleSaveDTO;
import com.example.blog.Pojo.entity.Article;
import com.example.blog.Pojo.entity.Tag;
import com.example.blog.Pojo.vo.ArticleVo;
import com.example.blog.Service.ArticleService;
import org.apache.ibatis.annotations.Delete;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.web.ServerProperties;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/article")
public class ArticleController {
    @Autowired
    private ArticleService articleService;

    @GetMapping("/{id}")
    public Result<ArticleVo> getArticle(@PathVariable Long id) {
        return articleService.getById(id);
    }
    @PostMapping("/save")
    public Result<String> save(@RequestBody ArticleSaveDTO articleSaveDTO) {
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

    @GetMapping("/tags/{articleId}")
    public Result<List<Tag>> getArticleTags(@PathVariable Long articleId) {
        List<Tag> tags = articleService.getArticleTags(articleId);
        return Result.success(tags);
    }
}