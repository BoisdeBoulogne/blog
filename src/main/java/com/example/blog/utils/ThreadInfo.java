package com.example.blog.utils;

public class ThreadInfo {
    private static ThreadLocal<Long> thread = new ThreadLocal<>();
    public static void setThread(Long id){
        thread.set(id);
    }
    public static Long getThread(){
        return thread.get();
    }
}
