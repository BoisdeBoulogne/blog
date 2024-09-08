package com.example.blog.Mapper;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface Tag2ArticlesMapper {
    @Select("select * from tag2articles where articles_id = #{articleId}")
    List<Long> getTagsIdByArticleId(Long articleId);

    @Insert("insert into tag2articles (tag_id, articles_id) VALUES (#{tagId},#{articleId})")
    void insert(Long articleId, Long tagId);
}
