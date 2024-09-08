package com.example.blog.Service;

import com.example.blog.Pojo.Result.PageResult;
import com.example.blog.Pojo.Result.Result;
import com.example.blog.Pojo.dto.UserLoginDTO;

public interface UserService {
    Result<String> login(UserLoginDTO userLogin);

    Result<String> follow(String targetUserId);

    PageResult getMyFans(int pageNum);
}
