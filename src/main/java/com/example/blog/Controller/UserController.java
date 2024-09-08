package com.example.blog.Controller;

import com.example.blog.Pojo.Result.PageResult;
import com.example.blog.Pojo.Result.Result;
import com.example.blog.Pojo.dto.UserLoginDTO;
import com.example.blog.Pojo.entity.Article;
import com.example.blog.Pojo.entity.User;
import com.example.blog.Pojo.vo.ArticleVo;
import com.example.blog.Pojo.vo.UserVo;
import com.example.blog.Service.UserService;
import com.github.pagehelper.Page;
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


    @PutMapping("/follow/{targetUserNickname}")
    public Result<String> follow(@PathVariable String targetUserNickname) {
        return userService.follow(targetUserNickname);
    }

    @GetMapping("/myFans/{pageNum}")
    public Result<PageResult<UserVo>> getMyFans(@PathVariable int pageNum) {
        PageResult<UserVo> page = userService.getMyFans(pageNum);
        return Result.success(page);
    }

    @GetMapping("/history/{pageNum}")
    public Result<PageResult<ArticleVo>> getHistory(@PathVariable int pageNum) {
        PageResult<ArticleVo> pageResult = userService.getHistory(pageNum);
        return Result.success(pageResult);
    }
}
