package com.example.blog.Pojo.vo;

import lombok.Data;

@Data
public class UserSpaceVO {
    private Integer followerCount; // 粉丝数
    private Integer followingCount; // 关注数
    private String phoneNumber;
    private Integer collectionCount;
    private String nickname;
    private String avatarUrl;
}
