package com.example.blog.Controller;

import com.example.blog.Mapper.ArticleMapper;
import com.example.blog.Mapper.CollectMapper;
import com.example.blog.Mapper.LikeMapper;
import com.example.blog.Pojo.Result.PageResult;
import com.example.blog.Pojo.Result.Result;
import com.example.blog.Pojo.dto.ArticleStatus;
import com.example.blog.Pojo.dto.KeyWordDTO;
import com.example.blog.Pojo.vo.ArticleVoForPre;
import com.example.blog.Service.ArticleService;
import com.github.pagehelper.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/home")
public class HomePageController {
    @Autowired
    ArticleService articleService;

    //主页数据
    @GetMapping()
    public Result<List<ArticleVoForPre>> getHotArticles() {
        List<ArticleVoForPre> articles = articleService.forHomePage();
        return Result.success(articles);
    }

    @GetMapping("/keyWord")
    public Result<PageResult<ArticleVoForPre>> getHotArticlesByKeyWord(KeyWordDTO dto) {
        PageResult<ArticleVoForPre> page = articleService.getByKeyWord(dto);
        return Result.success(page);
    }
}




