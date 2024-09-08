package com.example.blog.Mapper;

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
}
