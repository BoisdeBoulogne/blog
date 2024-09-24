package com.example.blog.Controller;

import com.example.blog.Pojo.Result.PageResult;
import com.example.blog.Pojo.Result.Result;
import com.example.blog.Pojo.dto.KeyWordDTO;
import com.example.blog.Pojo.vo.ArticleVoForPre;
import com.example.blog.Pojo.vo.HomePageVo;
import com.example.blog.Service.ArticleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/home")
public class HomePageController {
    @Autowired
    ArticleService articleService;

    //主页数据
    @GetMapping("/{pageNum}")
    public Result<HomePageVo> getHotArticles(@PathVariable int pageNum) {
        return articleService.forHomePage(pageNum);
    }

    @GetMapping("/keyWord")
    public Result<PageResult<ArticleVoForPre>> getHotArticlesByKeyWord(@RequestBody KeyWordDTO dto) {
        return articleService.getByKeyWord(dto);
    }
}




