package com.example.blog.Controller;

import com.example.blog.Pojo.Result.Result;
import com.example.blog.Pojo.entity.Tag;
import com.example.blog.Service.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/tags")
public class TagController {
    @Autowired
    TagService tagService;
    @PostMapping("/save")
    public Result save(@RequestBody Tag tag) {
        return tagService.save(tag);
    }
    @GetMapping("/all")
    public Result<List<Tag>> getAllTags(){
        List<Tag> tags = tagService.getAllTags();
        return Result.success(tags);
    }
}
