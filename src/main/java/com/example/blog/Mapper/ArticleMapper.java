package com.example.blog.Mapper;

import com.example.blog.Pojo.dto.ArticleStatus;
import com.example.blog.Pojo.entity.Article;
import com.example.blog.Pojo.vo.ArticleVo;
import com.example.blog.Pojo.vo.ArticleVoForPre;
import com.github.pagehelper.Page;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

@Mapper
public interface ArticleMapper {
    @Select("select * from articles where id = #{id}")
    Article getById(Long id);

    void save(Article article);

    Page<Article> getUsefulById(Long userId);

    @Delete("delete from articles where id = #{id}")
    void deleteById(Long id);

    void update(Article article);

    List<Long> forHomePage();

    void likeOrCollect(Long articleId, Integer collects, Integer howMany);

    @Select("select id from articles where content like concat ('%',#{keyword},'%')")
    List<Long> getIdsByKeyWord(String keyword);
}
