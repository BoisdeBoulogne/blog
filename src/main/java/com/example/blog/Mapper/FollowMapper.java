package com.example.blog.Mapper;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface FollowMapper {
    @Insert("insert into follow (follower_id, leader_id) VALUES (#{follower_id},#{leader_id})")
    void insert(Long follower_id, Long leader_id);

    @Select("select follower_id from follow where leader_id = #{currId}")
    List<Long> getFansById(Long currId);

    @Select("select follower_id from follow where leader_id = #{currId}")
    List<Long> getLeadersById(Long currId);

    @Select("select count(*) from follow where leader_id = #{targetId} and follower_id = #{shootId}")
    Integer getCount(Long shootId, Long targetId);

    @Delete("delete from follow where follower_id = #{followId} and leader_id = #{targetId}")
    void delete(Long followId, Long targetId);
}
