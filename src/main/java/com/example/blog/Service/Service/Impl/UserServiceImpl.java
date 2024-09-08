package com.example.blog.Service.Service.Impl;

import com.example.blog.Mapper.FollowMapper;
import com.example.blog.Mapper.UserMapper;
import com.example.blog.Pojo.Result.PageResult;
import com.example.blog.Pojo.Result.Result;
import com.example.blog.Pojo.dto.UserLoginDTO;
import com.example.blog.Pojo.entity.User;
import com.example.blog.Pojo.vo.UserVo;
import com.example.blog.Service.UserService;
import com.example.blog.constants.OtherConstants;
import com.example.blog.utils.JwtToken;
import com.example.blog.utils.ThreadInfo;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class UserServiceImpl implements UserService {
    @Autowired
    UserMapper userMapper;
    @Autowired
    FollowMapper followMapper;
    @Autowired
    private JwtToken jwtToken;
    @Override
    public Result<String> login(UserLoginDTO userLogin) {

        User user = userMapper.login(userLogin);
        if (user == null) {
            return Result.error("账号或密码错误");
        }
        //比对账号，密码
        //jwt来验证
        Map<String,Object> claims = new HashMap<>();
        claims.put("userId", user.getId());//Jwt使用账号来标明是哪个用户
        String token = jwtToken.createToken(claims);
        return Result.success(token);
    }

    @Override
    public Result<String> follow(String targetUserNickname) {
        Long targetId = userMapper.getByNickname(targetUserNickname);
        if(targetId == null) {
            return Result.error("不存在的用户");
        }
        ThreadInfo.setThread(2L);
        Long shootId = ThreadInfo.getThread();
        log.info("目标关注用户id: {}",targetId);
        //todo 对应被关注者数据更新
        userMapper.shootAdd(shootId);
        userMapper.targetAdd(targetId);
        followMapper.insert(targetId,shootId);
        return Result.success();
    }

    @Override
    public PageResult getMyFans(int pageNum) {

        PageHelper.startPage(pageNum,OtherConstants.pageSize);
        ThreadInfo.setThread(3L);
        Long currId = ThreadInfo.getThread();
        List<Long> fansId = followMapper.getFansById(currId);
        List<UserVo> users = new ArrayList<>();
        for (Long id : fansId){
            UserVo userVo = userMapper.getVoById(id);
            users.add(userVo);
        }
        PageResult<UserVo> pageResult = new PageResult<>();
        pageResult.setTotal(users.size());
        pageResult.setList(users);
        return pageResult;
    }


}
