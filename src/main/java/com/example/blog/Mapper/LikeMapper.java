package com.example.blog.Mapper;

import com.example.blog.Pojo.dto.ArticleStatus;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface LikeMapper {
    @Insert("insert into likes (user_id, article_id) VALUES (#{userId},#{articleId})")
    void insert(Long articleId, Long userId);

    @Delete("delete from likes where user_id = #{userId} and article_id = #{articleId}")
    void delete(Long articleId, Long userId);

    @Select("select count(*) from likes where user_id = #{userId} and article_id = #{articleId}")
    int getCount(Long articleId, Long userId);
}

