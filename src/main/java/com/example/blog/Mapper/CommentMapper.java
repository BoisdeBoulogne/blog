package com.example.blog.Mapper;

import com.example.blog.Pojo.entity.Comments;
import com.example.blog.Pojo.vo.CommentVo;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface CommentMapper {
    @Insert("insert into comments (comment, user_id, article_id, user_nickname) VALUES (#{comment},#{userId},#{articleId},#{userNickname})")
    void insert(Comments comment);

    List<CommentVo> getCommentsByArticleId(Long id);
}
