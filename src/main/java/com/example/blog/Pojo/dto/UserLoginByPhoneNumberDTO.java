package com.example.blog.Pojo.dto;

import lombok.Data;

@Data
public class UserLoginByPhoneNumberDTO {
    private String phoneNumber;
    private String code;
}
