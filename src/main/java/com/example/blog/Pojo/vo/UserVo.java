package com.example.blog.Pojo.vo;

import lombok.Data;

@Data
public class UserVo {
    private String nickname;
    private Integer followerCount;
    private Integer followingCount;
    private String avatarUrl;
}
