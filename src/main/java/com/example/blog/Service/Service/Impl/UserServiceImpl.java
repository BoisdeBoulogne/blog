package com.example.blog.Service.Service.Impl;

import com.example.blog.Mapper.UserMapper;
import com.example.blog.Pojo.Result.Result;
import com.example.blog.Pojo.dto.UserLoginDTO;
import com.example.blog.Pojo.entity.User;
import com.example.blog.Service.UserService;
import com.example.blog.utils.JwtToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    UserMapper userMapper;
    @Autowired
    private JwtToken jwtToken;
    @Override
    public Result<String> login(UserLoginDTO userLogin) {

        User user = userMapper.login(userLogin);
        if (user == null) {
            return Result.error("账号或密码错误");
        }
        //比对账号，密码
        //jwt来验证
        Map<String,Object> claims = new HashMap<>();
        claims.put("account", user.getAccount());//Jwt使用账号来标明是哪个用户
        String token = jwtToken.createToken(claims);
        return Result.success(token);
    }
}
