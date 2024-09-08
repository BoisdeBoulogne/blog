package com.example.blog.Mapper;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface Tag2ArticlesMapper {
    @Select("select tag_id from tag2articles where articles_id = #{articleId}")
    List<Long> getTagsIdByArticleId(Long articleId);

    @Insert("insert into tag2articles (tag_id, articles_id) VALUES (#{tagId},#{articleId})")
    void insert(Long articleId, Long tagId);

    @Select("select tag_id from tag2articles where articles_id = #{id}" )
    List<Long> getByArticleId(Long id);

    @Delete("delete from tag2articles where articles_id = #{id}")
    void deleteByArticleId(Long id);
}
