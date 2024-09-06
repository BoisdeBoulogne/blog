package com.example.blog.Mapper;

import com.example.blog.Pojo.entity.Tag;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface TagMapper {

    @Insert("insert into tag (tag_name) values (#{tagName})")
    void insert(Tag tag);

    @Select("select * from tag")
    List<Tag> getAllTags();

    @Select("select * from tag where tag_name = #{tagName}")
    Tag getByTagName(String tagName);
}
