package com.example.blog.Mapper;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.time.LocalDateTime;
import java.util.List;

@Mapper
public interface HistoryMapper {

    @Insert("insert into history (articles_id,user_id,view_time) VALUES (#{id},#{userId},#{now}) " +
            "on duplicate key update view_time = #{now}")
    void insert(Long id, Long userId, LocalDateTime now);

    @Select("select articles_id from history where user_id = #{currId} order by view_time desc")
    List<Long> getArticleIdsByUserId(Long currId);

    @Delete("delete from history where articles_id = #{id}")
    void deleteByArticleId(Long id);
}
