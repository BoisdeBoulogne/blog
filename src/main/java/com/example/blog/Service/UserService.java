package com.example.blog.Service;

import com.example.blog.Pojo.Result.PageResult;
import com.example.blog.Pojo.Result.Result;
import com.example.blog.Pojo.dto.*;
import com.example.blog.Pojo.vo.ArticleVoForPre;
import com.example.blog.Pojo.vo.UserVo;

import java.util.List;

public interface UserService {
    Result<String> login(UserLoginDTO userLogin);

    Result<String> follow(Long targetUserId);

    List<UserVo> getMyFans();


    Result<String> collect(Long articleId);

    Result<PageResult<ArticleVoForPre>> getMyCollect(int pageNum);

    Result<PageResult<ArticleVoForPre>> getMyHistory(int pageNum);

    Result<String> like(Long articleId);

    Result<String> removeLike(Long articleId);

    List<UserVo> getMyLeaders();

    Result<String> removeCollect(Long articleId);

    void comment(CommentDTO commentDTO);

    Result<String> removeFollow(Long targetUserId);

    Result<String> getCode(String phoneNumber);

    Result<String> signIn(UserSignInDTO userSignInDTO);

    Result<String> loginByPhoneNumber(UserLoginByPhoneNumberDTO userLoginByPhoneNumberDTO);

    Result<String> resetPassword(UserResetPassword userResetPassword);

    void updateImg(String imgUrl);
}
