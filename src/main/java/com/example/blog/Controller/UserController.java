package com.example.blog.Controller;

import com.example.blog.Pojo.Result.PageResult;
import com.example.blog.Pojo.Result.Result;
import com.example.blog.Pojo.dto.UserLoginDTO;

import com.example.blog.Pojo.vo.ArticleVoForPre;
import com.example.blog.Pojo.vo.UserVo;
import com.example.blog.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    UserService userService;
    @PostMapping("/login")
    public Result<String> login(@RequestBody UserLoginDTO userLoginDTO) {
        return userService.login(userLoginDTO);
    }

    //关注用户 已测试
    @PutMapping("/follow/{targetUserNickname}")
    public Result<String> follow(@PathVariable String targetUserNickname) {
        return userService.follow(targetUserNickname);
    }
    //查看自己粉丝 //已测试
    @GetMapping("/myFans/{pageNum}")
    public Result<PageResult<UserVo>> getMyFans(@PathVariable int pageNum) {
        PageResult<UserVo> page = userService.getMyFans(pageNum);
        return Result.success(page);
    }
    //关注列表 已测试
    @GetMapping("/myLeaders/{pageNum}")
    public Result<PageResult<UserVo>> getMyLeader(@PathVariable int pageNum) {
        PageResult<UserVo> page = userService.getMyLeaders(pageNum);
        return Result.success(page);
    }


    //收藏文章 已测试
    @PutMapping("/collect/{articleId}")
    public Result<String> collectArticleById(@PathVariable Long articleId){
        return  userService.collect(articleId);

    }
    //移除收藏 已测试
    @PutMapping("/removeCollect/{articleId}")
    public Result<String> removeCollect(@PathVariable Long articleId){

        return userService.removeCollect(articleId);
    }
    //查看收藏 已测试
    @GetMapping("/myCollect/{pageNum}")
    public Result<PageResult<ArticleVoForPre>> getMyCollect(@PathVariable int pageNum){
        return userService.getMyCollect(pageNum);
    }
    //查看历史记录 已测试
    @GetMapping("/myHistory/{pageNum}")
    public Result<PageResult<ArticleVoForPre>> getMyHistory(@PathVariable int pageNum){
        return userService.getMyHistory(pageNum);
    }
    //添加喜欢 已测试
    @PutMapping("/like/{articleId}")
    public Result<String> like(@PathVariable Long articleId) {
        return  userService.like(articleId);
    }
    //移除喜欢 已测试
    @DeleteMapping("/removeLike/{articleId}")
    public Result<String> removeLike(@PathVariable Long articleId) {
        return userService.removeLike(articleId);
    }



}
