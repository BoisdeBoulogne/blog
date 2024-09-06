package com.example.blog.Mapper;

import com.example.blog.Pojo.entity.Article;
import com.example.blog.Pojo.vo.ArticleVo;
import com.github.pagehelper.Page;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface ArticleMapper {
    @Select("select * from articles where id = #{id}")
    Article getById(Integer id);

    void save(Article article);

    Page<ArticleVo> getUsefulById(Long userId);
}
