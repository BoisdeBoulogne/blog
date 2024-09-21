package com.example.blog.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Component

public class Lock {
    @Autowired
    StringRedisTemplate stringRedisTemplate;

}
