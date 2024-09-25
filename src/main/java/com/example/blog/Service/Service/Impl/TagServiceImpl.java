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
        List<TagVo> result = null;
        String jsonString = stringRedisTemplate.opsForValue().get(key);
        if (StrUtil.isNotBlank(jsonString)) {
            result = JSONUtil.toList(jsonString, TagVo.class);
            return result;
        }
        if (result != null) {
            return null;
        }
        if (jsonString == null) {

            try {
                boolean lock = tryLock("tagLock");
                if (!lock){
                    Thread.sleep(50);
                    return getAllTags();
                }
                List<Tag> tags = tagMapper.getAllTags();

                if (tags == null || tags.size() == 0) {
                    stringRedisTemplate.opsForValue().set(key,"", 5, TimeUnit.MINUTES);
                    return null;
                }
                List<TagVo> tagVos = new ArrayList<>();
                for (Tag tag : tags) {
                    Integer count = tag2ArticlesMapper.getCountByTagId(tag.getId());
                    TagVo tagVo = new TagVo();
                    tagVo.setTagName(tag.getTagName());
                    tagVo.setId(tag.getId());
                    tagVo.setCount(count);
                    tagVos.add(tagVo);
                }
                log.info(JSONUtil.toJsonStr(tagVos));
                stringRedisTemplate.opsForValue().set(key,JSONUtil.toJsonStr(tagVos), 30, TimeUnit.MINUTES);
                return tagVos;
            }catch (Exception e){
                e.printStackTrace();
            }
            finally {
                unlock("tagLock");
            }

        }


        return result;
    }

    private boolean tryLock(String key2){
        Boolean flag = stringRedisTemplate.opsForValue().setIfAbsent(key2,"1",30,TimeUnit.SECONDS);
        return BooleanUtil.isTrue(flag);
    }

    private void unlock(String key2){
        stringRedisTemplate.delete(key2);
    }
}
