package com.example.blog.Mapper;

import com.example.blog.Pojo.dto.ArticleStatus;
import com.github.pagehelper.Page;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface CollectMapper {

    @Insert("insert into collect (user_id, article_id) values (#{userId},#{articleId})")
    void insert(Long userId, Long articleId);

    @Select("select article_id from collect where user_id = #{currId}")
    Page<Long> getByUserId(Long currId);

    @Delete("delete from collect where user_id = #{userId} and article_id = #{articleId}")
    void delete(Long userId, Long articleId);
    @Select("select count(*) from collect where article_id = #{articleId} and user_id = #{userId}")
    int getCount(Long userId, Long articleId);
}
