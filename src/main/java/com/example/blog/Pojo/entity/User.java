package com.example.blog.Pojo.entity;

import lombok.Data;

@Data
public class User {
    private Long id;              //id
    private String password;      // 密码
    private String nickname;      // 昵称
    private String avatarUrl;     // 头像URL地址
    private Integer followerCount; // 粉丝数
    private Integer followingCount; // 关注数
    private String phoneNumber;

}