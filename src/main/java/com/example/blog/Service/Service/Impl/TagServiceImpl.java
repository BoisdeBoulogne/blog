package com.example.blog.Service.Service.Impl;

import cn.hutool.core.util.BooleanUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.example.blog.Mapper.TagMapper;
import com.example.blog.Pojo.Result.Result;
import com.example.blog.Pojo.entity.Tag;
import com.example.blog.Service.TagService;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
@Slf4j
public class TagServiceImpl implements TagService {
    @Autowired
    private TagMapper tagMapper;
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
    public List<Tag> getAllTags() {
        List<Tag> result = null;
        String jsonString = stringRedisTemplate.opsForValue().get(key);
        if (StrUtil.isNotBlank(jsonString)) {
            result = JSONUtil.toList(jsonString, Tag.class);
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
                log.info(JSONUtil.toJsonStr(tags));
                stringRedisTemplate.opsForValue().set(key,JSONUtil.toJsonStr(tags), 30, TimeUnit.MINUTES);
                return tags;
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
