package com.example.blog.Service;

import com.example.blog.Pojo.Result.PageResult;
import com.example.blog.Pojo.Result.Result;
import com.example.blog.Pojo.dto.CommentDTO;
import com.example.blog.Pojo.dto.UserLoginDTO;
import com.example.blog.Pojo.vo.ArticleVoForPre;
import com.example.blog.Pojo.vo.UserVo;

public interface UserService {
    Result<String> login(UserLoginDTO userLogin);

    Result<String> follow(Long targetUserId);

    PageResult getMyFans(int pageNum);


    Result<String> collect(Long articleId);

    Result<PageResult<ArticleVoForPre>> getMyCollect(int pageNum);

    Result<PageResult<ArticleVoForPre>> getMyHistory(int pageNum);

    Result<String> like(Long articleId);

    Result<String> removeLike(Long articleId);

    PageResult<UserVo> getMyLeaders(int pageNum);

    Result<String> removeCollect(Long articleId);

    void comment(CommentDTO commentDTO);

    Result<String> removeFollow(Long targetUserId);
}
