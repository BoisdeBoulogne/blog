package com.example.blog.hander;

import com.example.blog.Pojo.Result.Result;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.sql.SQLIntegrityConstraintViolationException;

public class GlobalExceptionHandler {
    @ExceptionHandler
    public Result exceptionHandler(SQLIntegrityConstraintViolationException ex){
        //Duplicate entry 'zhangsan' for key 'employee.idx_username'
        String message = ex.getMessage();
        if(message.contains("Duplicate entry")){
            String[] split = message.split(" ");
            String username = split[2];
            String msg = username + "已存在";
            return Result.error(msg);
        }else{
            return Result.error("未知错误，请联系管理员");
        }
    }
    public Result exceptionHandler(RuntimeException ex){
        String message = ex.getMessage();
        return Result.error("未知错误，请联系管理员");

    }
}
