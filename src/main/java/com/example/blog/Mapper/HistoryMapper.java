package com.example.blog.Mapper;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface HistoryMapper {

    @Insert("insert into history (articles_id,user_id) VALUES (#{id},#{userId}) ")
    void insert(Long id, Long userId);

    @Select("select articles_id from history where user_id = #{currId}")
    List<Long> getArticleIdsByUserId(Long currId);
}
