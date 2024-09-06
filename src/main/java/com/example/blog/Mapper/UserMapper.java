package com.example.blog.Mapper;

import com.example.blog.Pojo.dto.UserLoginDTO;
import com.example.blog.Pojo.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface UserMapper {
    @Select("select * from user where password = #{password} and account = #{account}")
    User login(UserLoginDTO userLogin);
}
