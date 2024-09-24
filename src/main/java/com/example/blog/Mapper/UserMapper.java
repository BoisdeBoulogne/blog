package com.example.blog.Mapper;

import com.example.blog.Pojo.dto.UserLoginDTO;
import com.example.blog.Pojo.dto.UserResetPassword;
import com.example.blog.Pojo.dto.UserSignInDTO;
import com.example.blog.Pojo.entity.User;
import com.example.blog.Pojo.vo.UserVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

@Mapper
public interface UserMapper {
    @Select("select * from user where password = #{password} and nickname = #{nickname}")
    User login(UserLoginDTO userLogin);

    @Select("select *from user where id = #{id}")
    User getById(int id);

    @Select("select id from user where nickname = #{targetUserNickname}")
    Long getByNickname(String targetUserNickname);

    UserVo getVoById(Long id);



    @Select("select nickname from user where id = #{userId}")
    String getNickNameById(Long userId);
    @Select("select count(*) from user where id = #{targetId}")
    Integer exist(Long targetId);

    void insert(User user);

    @Select("select count(*) from user where phone_number = #{phoneNumber}")
    int getByPhoneNumber(String phoneNumber);

    @Select("select id from user where phone_number = #{phoneNumber}")
    Long getIdByPhoneNumber(String phoneNumber);

    @Update("update user set password = #{resetPassword} where phone_number = #{phoneNumber}")
    void resetPassword(UserResetPassword userResetPassword);
    @Select("select count(*) from user where nickname = #{nickname} or phone_number = #{phoneNumber} ")
    Integer checkSignIn(UserSignInDTO userSignInDTO);

    void shootAddOrDelete(Long shootId, Integer add);

    void targetAddOrDelete(Long targetId, Integer add);

    @Update("update user set avatar_url = #{imgUrl} where id = #{userId} " )
    void updateImg(String imgUrl, Long userId);
}
