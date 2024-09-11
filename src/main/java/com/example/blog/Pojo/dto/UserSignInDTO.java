package com.example.blog.Pojo.dto;

import lombok.Data;

@Data
public class UserSignInDTO {
    private String phoneNumber;
    private String password;
    private String nickname;
    private String code;
    private String avatarUrl;
}
