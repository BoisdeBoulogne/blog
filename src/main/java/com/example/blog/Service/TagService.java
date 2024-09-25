package com.example.blog.Service;

import com.example.blog.Pojo.Result.Result;
import com.example.blog.Pojo.vo.TagVo;

import java.util.List;

public interface TagService {
    Result save(String tag);

    List<TagVo> getAllTags();
}
