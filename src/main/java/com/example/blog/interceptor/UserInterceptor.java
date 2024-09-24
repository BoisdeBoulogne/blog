package com.example.blog.interceptor;

import com.example.blog.utils.JwtToken;
import com.example.blog.utils.ThreadInfo;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Slf4j
@Component
public class UserInterceptor implements HandlerInterceptor {

    @Autowired
    private JwtToken jwtToken;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String requestURI = request.getRequestURI();
        String token = request.getHeader("Authorization");

        // 仅匹配 /article/{id}，id 只允许是数字
        if (requestURI.matches("/article/\\d+")) {
            log.info("Anonymous or logged-in access allowed for: " + requestURI);

            // 如果有 token，则尝试解析
            if (token != null && !token.isEmpty()) {
                try {
                    Long userId = jwtToken.parseToken(token).get("userId", Long.class);
                    log.info("Logged in user ID: " + userId);
                    ThreadInfo.setThread(userId);  // 将用户信息保存到 ThreadLocal 中
                } catch (Exception e) {
                    log.error("JWT parsing failed", e);
                    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                    return false;  // token 解析失败，返回401
                }
            }

            return true;  // 没有 token 或 token 解析成功，允许匿名访问
        }

        // 对于其他路径，如 /article/commonTags，强制要求 JWT 验证
        if (token == null || token.isEmpty()) {
            log.warn("Unauthorized access attempt to: " + requestURI);
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return false;  // 没有 token，返回 401
        }

        try {
            Long userId = jwtToken.parseToken(token).get("userId", Long.class);
            log.info("Logged in user ID: " + userId);
            ThreadInfo.setThread(userId);
            return true;  // token 解析成功，允许继续请求
        } catch (Exception e) {
            log.error("JWT parsing failed", e);
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return false;  // token 解析失败，返回 401
        }
    }
}
