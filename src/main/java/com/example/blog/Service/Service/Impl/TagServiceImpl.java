package com.example.blog.Service.Service.Impl;

import cn.hutool.core.util.BooleanUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.example.blog.Mapper.Tag2ArticlesMapper;
import com.example.blog.Mapper.TagMapper;
import com.example.blog.Pojo.Result.Result;
import com.example.blog.Pojo.entity.Tag;
import com.example.blog.Pojo.vo.TagVo;
import com.example.blog.Service.TagService;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
@Slf4j
public class TagServiceImpl implements TagService {
    @Autowired
    private TagMapper tagMapper;
    @Autowired
    private Tag2ArticlesMapper tag2ArticlesMapper;
    @Autowired
    StringRedisTemplate stringRedisTemplate;
    private final String key = "tags";
    @Override
    public Result save(String tag) {
        Integer count = tagMapper.getByTagName(tag);
        if (count > 0) {
            return Result.error("已存在的标签");
        }
        stringRedisTemplate.delete(key);
        tagMapper.insert(tag);
        return Result.success();
    }

    @Override
    public List<TagVo> getAllTags() {


        List<Tag> tags = tagMapper.getAllTags();

        List<TagVo> tagVos = new ArrayList<>();
        for (Tag tag : tags) {
            Integer count = tag2ArticlesMapper.getCountByTagId(tag.getId());
            TagVo tagVo = new TagVo();
            tagVo.setTagName(tag.getTagName());
            tagVo.setId(tag.getId());
            tagVo.setCount(count);
            tagVos.add(tagVo);
        }

        return tagVos;

    }


}
