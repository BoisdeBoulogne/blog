package com.example.blog.Mapper;

import com.example.blog.Pojo.entity.Tag;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface TagMapper {

    @Insert("insert into tag (tag_name) values (#{tag})")
    void insert(String tag);

    @Select("select * from tag")
    List<Tag> getAllTags();

    @Select("select count(*) from tag where tag_name = #{tagName}")
    Integer getByTagName(String tagName);

    @Select("select tag_name from tag where id = #{tagId}")
    String getNameById(Long tagId);

    @Select("select * from tag where id = #{tagId}")
    Tag getById(Long tagId);
}
