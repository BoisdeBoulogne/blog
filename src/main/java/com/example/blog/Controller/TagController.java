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
    @PostMapping("/save/tagName")
    public Result save(@PathVariable String tagName) {
        return tagService.save(tagName);
    }
    @GetMapping("/all")
    public Result<List<Tag>> getAllTags(){
        List<Tag> tags = tagService.getAllTags();
        if(tags==null){
            return Result.error("暂时没有标签");
        }
        return Result.success(tags);
    }


}
