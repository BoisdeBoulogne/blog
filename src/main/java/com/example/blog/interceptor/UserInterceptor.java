package com.example.blog.interceptor;

import com.example.blog.utils.JwtToken;
import com.example.blog.utils.ThreadInfo;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.Map;

@Slf4j
@Component
public class UserInterceptor implements HandlerInterceptor {
    @Autowired
    private JwtToken jwtToken;
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String token = request.getHeader("Authorization");
        if (token == null) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        }
        try {
            Long userId = jwtToken.parseToken(token).get("userId", Long.class);
            log.info(userId.toString());
            ThreadInfo.setThread(userId);
            return true;
        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return false;
        }
    }
}
