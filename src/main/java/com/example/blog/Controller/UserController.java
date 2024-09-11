package com.example.blog.Controller;

import com.example.blog.Pojo.Result.PageResult;
import com.example.blog.Pojo.Result.Result;
import com.example.blog.Pojo.dto.*;

import com.example.blog.Pojo.vo.ArticleVoForPre;
import com.example.blog.Pojo.vo.UserVo;
import com.example.blog.Service.UserService;
import com.example.blog.utils.ThreadInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    UserService userService;

    //获取验证码
    @GetMapping("/code/{phoneNumber}")
    public Result<String> getCode(@PathVariable String phoneNumber) {
        return userService.getCode(phoneNumber);
    }
    //注册
    @PostMapping("/signIn")
    public Result<String> signIn(@RequestBody UserSignInDTO userSignInDTO) {
        return userService.signIn(userSignInDTO);
    }
    //手机号验证码登录
    @PostMapping("loginByPhoneNumber")
    public Result<String> loginByPhoneNumber(@RequestBody UserLoginByPhoneNumberDTO userLoginByPhoneNumberDTO) {
        return userService.loginByPhoneNumber(userLoginByPhoneNumberDTO);
    }
    //账号密码登录
    @PostMapping("/login")
    public Result<String> login(@RequestBody UserLoginDTO userLoginDTO) {
        return userService.login(userLoginDTO);
    }

    @PostMapping("/resetPassword")
    public Result<String> resetPassword(@RequestBody UserResetPassword userResetPassword){
        return userService.resetPassword(userResetPassword);
    }

    //关注用户 已测试
    @PutMapping("/follow/{targetUserId}")
    public Result<String> follow(@PathVariable Long targetUserId) {
        return userService.follow(targetUserId);
    }
    //取消关注
    @PutMapping("/unfollow/{targetUserId}")
    public Result<String> removeFollow(@PathVariable Long targetUserId) {
        return userService.removeFollow(targetUserId);
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

    //新增评论
    @PostMapping("/comment")
    public Result<String> comment(@RequestBody CommentDTO commentDTO) {
        ThreadInfo.setThread(2L);
        userService.comment(commentDTO);
        return Result.success();
    }




}
