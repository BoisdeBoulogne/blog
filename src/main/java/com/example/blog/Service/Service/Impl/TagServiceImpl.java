package com.example.blog.Service.Service.Impl;

import com.example.blog.Mapper.TagMapper;
import com.example.blog.Pojo.Result.Result;
import com.example.blog.Pojo.entity.Tag;
import com.example.blog.Service.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TagServiceImpl implements TagService {
    @Autowired
    private TagMapper tagMapper;
    @Override
    public Result save(Tag tag) {
        Tag test = tagMapper.getByTagName(tag.getTagName());
        if (test != null) {
            return Result.error("已存在的标签");
        }
        tagMapper.insert(tag);
        return Result.success();
    }

    @Override
    public List<Tag> getAllTags() {
        return tagMapper.getAllTags();
    }
}
