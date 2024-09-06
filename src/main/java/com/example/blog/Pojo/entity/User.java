package com.example.blog.Pojo.entity;

import lombok.Data;

@Data
public class User {
    private Long id;              //id
    private String account;       // 账号
    private String password;      // 密码
    private String nickname;      // 昵称
    private Integer isAdmin;      // 管理员标识
    private String avatarUrl;     // 头像URL地址
    private Integer followerCount; // 粉丝数
    private Integer followingCount; // 关注数

}