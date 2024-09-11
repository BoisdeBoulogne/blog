package com.example.blog.Pojo.dto;

import lombok.Data;

@Data
public class UserResetPassword {
    private String phoneNumber;
    private String resetPassword;
    private String code;
}
