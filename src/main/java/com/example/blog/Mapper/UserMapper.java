package com.example.blog.Mapper;

import com.example.blog.Pojo.dto.UserLoginDTO;
import com.example.blog.Pojo.entity.User;
import com.example.blog.Pojo.vo.UserVo;
import com.github.pagehelper.Page;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

@Mapper
public interface UserMapper {
    @Select("select * from user where password = #{password} and account = #{account}")
    User login(UserLoginDTO userLogin);

    @Select("select *from user where id = #{id}")
    User getById(int id);

    @Select("select id from user where nickname = #{targetUserNickname}")
    Long getByNickname(String targetUserNickname);

    UserVo getVoById(Long id);

    @Update("update user set following_count = following_count+1 where id = #{shootId}")
    void shootAdd(Long shootId);

    @Update("update user set follower_count = following_count+1 where id = #{targetId}")
    void targetAdd(Long targetId);

    @Select("select nickname from user where id = #{userId}")
    String getNickNameById(Long userId);
    @Select("select count(*) from user where id = #{targetId}")
    Integer exist(Long targetId);

    void insert(User user);
}
