package com.example.blog.Mapper;

import com.example.blog.Pojo.entity.Article;
import com.example.blog.Pojo.vo.ArticleVo;
import com.github.pagehelper.Page;
import lombok.Data;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface ArticleMapper {
    @Select("select * from articles where id = #{id}")
    Article getById(Integer id);

    void save(Article article);

    Page<ArticleVo> getUsefulById(Long userId);

    @Delete("delete from articles where id = #{id}")
    void deleteById(Integer id);

    void update(Article article);
}
