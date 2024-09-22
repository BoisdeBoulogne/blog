package com.example.blog.Service.Service.Impl;
import com.example.blog.Mapper.*;
import com.example.blog.Pojo.Result.PageResult;
import com.example.blog.Pojo.Result.Result;
import com.example.blog.Pojo.dto.*;
import com.example.blog.Pojo.entity.Article;
import com.example.blog.Pojo.entity.Comments;
import com.example.blog.Pojo.entity.Tag;
import com.example.blog.Pojo.entity.User;
import com.example.blog.Pojo.vo.ArticleVoForPre;
import com.example.blog.Pojo.vo.UserVo;
import com.example.blog.Service.UserService;
import com.example.blog.constants.OtherConstants;
import com.example.blog.utils.JwtToken;
import com.example.blog.utils.ThreadInfo;
import com.github.pagehelper.PageHelper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;

@Slf4j
@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private FollowMapper followMapper;
    @Autowired
    private JwtToken jwtToken;
    @Autowired
    private HistoryMapper historyMapper;
    @Autowired
    private ArticleMapper articleMapper;
    @Autowired
    private CollectMapper collectMapper;
    @Autowired
    private TagMapper tagMapper;
    @Autowired
    private Tag2ArticlesMapper tag2ArticlesMapper;
    @Autowired
    private LikeMapper likeMapper;
    @Autowired
    CommentMapper commentMapper;
    @Autowired
    StringRedisTemplate stringRedisTemplate;

    @Override
    public Result<String> getCode(String phoneNumber) {
        if ( phoneNumber == null||!judgePhoneNumber(phoneNumber)) {
            return Result.error("电话号码格式错误或为空");
        }
        String code = randomString();
        stringRedisTemplate.opsForValue().set("code:"+ phoneNumber,code,5, TimeUnit.MINUTES);
        log.info("code:{}",code);
        return Result.success(code);
    }

    @Override
    public Result<String> signIn(UserSignInDTO userSignInDTO) {
        String phoneNumber = userSignInDTO.getPhoneNumber();
        if ( phoneNumber == null||!judgePhoneNumber(phoneNumber)) {
            return Result.error("电话号码格式错误或为空");
        }
        String code = userSignInDTO.getCode();
        String realCode = stringRedisTemplate.opsForValue().get("code:"+ phoneNumber);
        if (code ==null || realCode == null || !code.equals(realCode)) {
            return Result.error("验证码错误或没有获取验证码");
        }
        User user = new User();
        BeanUtils.copyProperties(userSignInDTO,user);
        try {
            userMapper.insert(user);
        } catch (Exception e) {
            if (e instanceof DuplicateKeyException) {
                return Result.error("已存在的用户");
            }
        }
        return Result.success("注册成功");
    }

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
    public Result<String> loginByPhoneNumber(UserLoginByPhoneNumberDTO userLoginByPhoneNumberDTO) {
        String phoneNumber = userLoginByPhoneNumberDTO.getPhoneNumber();
        int count = userMapper.getByPhoneNumber(phoneNumber);
        if (count == 0 ){
            return Result.error("未注册的用户");
        }
        String realCode = stringRedisTemplate.opsForValue().get("code:"+ phoneNumber);
        if (realCode == null || !realCode.equals(userLoginByPhoneNumberDTO.getCode())) {
            return Result.error("未发送验证码或验证码错误");
        }
        Long userId = userMapper.getIdByPhoneNumber(phoneNumber);
        Map<String,Object> claims = new HashMap<>();
        claims.put("userId", userId);//Jwt使用账号来标明是哪个用户
        String token = jwtToken.createToken(claims);
        return Result.success(token);
    }

    @Override
    public Result<String> resetPassword(UserResetPassword userResetPassword) {
        String phoneNumber = userResetPassword.getPhoneNumber();
        int count = userMapper.getByPhoneNumber(phoneNumber);
        if (count == 0 ){
            return Result.error("未注册的用户");
        }
        String code = userResetPassword.getCode();
        String realCode = stringRedisTemplate.opsForValue().get("code:"+ phoneNumber);
        if (code ==null || realCode == null || !code.equals(realCode)) {
            return Result.error("总之出现错误");
        }
        userMapper.resetPassword(userResetPassword);
        return Result.success();
    }

    @Override
    public Result<String> follow(Long targetId) {
        Long shootId = ThreadInfo.getThread();
        if (shootId == null){
            return Result.error("未登录");
        }
        Integer exist = userMapper.exist(targetId);
        if(exist == 0) {
            return Result.error("不存在的用户");
        }

        Integer count = followMapper.getCount(shootId,targetId);
        if (count > 0) {
            return Result.error("已关注");
        }
        log.info("目标关注用户id: {}",targetId);
        userMapper.shootAdd(shootId);
        userMapper.targetAdd(targetId);
        followMapper.insert(targetId,shootId);
        return Result.success();
    }

    @Override
    public Result<String> removeFollow(Long targetId) {
        Integer exist = userMapper.exist(targetId);
        if(exist == 0) {
            return Result.error("不存在的用户");
        }
        Long followId = ThreadInfo.getThread();
        if (followId == null){
            return Result.error("未登录");
        }
        Integer count = followMapper.getCount(followId,targetId);
        if (count == 0) {
            return Result.error("未关注");
        }
        followMapper.delete(followId,targetId);
        return Result.success();
    }

    @Override
    public PageResult<UserVo> getMyFans(int pageNum) {

        Long currId = ThreadInfo.getThread();

        PageHelper.startPage(pageNum,OtherConstants.pageSize);



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
    @Override
    public PageResult<UserVo> getMyLeaders(int pageNum){
        PageHelper.startPage(pageNum,OtherConstants.pageSize);
        Long currId = ThreadInfo.getThread();
        List<Long> leadersId = followMapper.getLeadersById(currId);
        List<UserVo> users = new ArrayList<>();
        for (Long id : leadersId){
            UserVo userVo = userMapper.getVoById(id);
            users.add(userVo);
        }
        PageResult<UserVo> pageResult = new PageResult<>();
        pageResult.setTotal(users.size());
        pageResult.setList(users);
        return pageResult;
    }



    @Override
    public Result<String> collect(Long articleId) {
        ThreadInfo.setThread(2L);
        Long userId = ThreadInfo.getThread();
        int count = collectMapper.getCount(userId,articleId);
        if (count != 0){
            return Result.error("已经收藏过了");
        }
        collectMapper.insert(userId,articleId);
        articleMapper.likeOrCollect(articleId,OtherConstants.collects,OtherConstants.add);
        return Result.success();
    }

    @Override
    public Result<String> removeCollect(Long articleId) {
        Long userId = ThreadInfo.getThread();
        int count = collectMapper.getCount(userId,articleId);
        if (count == 0){
            return Result.error("没有收藏过");
        }
        collectMapper.delete(userId,articleId);
        return Result.success();
    }

    @Override
    public Result<PageResult<ArticleVoForPre>> getMyCollect(int pageNum) {

        PageHelper.startPage(pageNum,OtherConstants.pageSize);
        Long currId = ThreadInfo.getThread();
        List<Long> articleIds = collectMapper.getByUserId(currId);
        List<ArticleVoForPre> articles = getArticleVoForPre(articleIds);
        PageResult<ArticleVoForPre> pageResult = new PageResult<>();
        pageResult.setTotal(articles.size());
        pageResult.setList(articles);
        return Result.success(pageResult);
    }



    public List<ArticleVoForPre> getArticleVoForPre(List<Long> articleIds) {
        List<ArticleVoForPre> articles = new ArrayList<>();
        for (Long id : articleIds){
            Article article = articleMapper.getById(id);
            ArticleVoForPre articleVoForPre = new ArticleVoForPre();
            BeanUtils.copyProperties(article,articleVoForPre);
            List<Long> tagIds = tag2ArticlesMapper.getTagsIdByArticleId(article.getId());
            List<Tag> tags = new ArrayList<>();
            for (Long tagId : tagIds){
                Tag tag = tagMapper.getById(tagId);
                tags.add(tag);
            }
            articleVoForPre.setTags(tags);
            articles.add(articleVoForPre);
        }
        return articles;
    }

    @Override
    public Result<PageResult<ArticleVoForPre>> getMyHistory(int pageNum) {
        PageHelper.startPage(pageNum,OtherConstants.pageSize);

        Long currId = ThreadInfo.getThread();
        List<Long> articleIds = historyMapper.getArticleIdsByUserId(currId);
        List<ArticleVoForPre> articles = getArticleVoForPre(articleIds);
        PageResult<ArticleVoForPre> pageResult = new PageResult<>();
        pageResult.setTotal(articles.size());
        pageResult.setList(articles);
        return Result.success(pageResult);
    }

    @Override
    public Result<String> like(Long articleId) {

        Long userId = ThreadInfo.getThread();
        int count = likeMapper.getCount(articleId,userId);
        if (count != 0){
            return Result.error("已经点赞过");
        }
        likeMapper.insert(articleId,userId);
        articleMapper.likeOrCollect(articleId,OtherConstants.likes,OtherConstants.add);
        return Result.success();
    }

    @Override
    public Result<String> removeLike(Long articleId) {
        int count = likeMapper.getCount(articleId,ThreadInfo.getThread());
        if (count == 0){
            return Result.error("还未点赞");
        }
        Long userId = ThreadInfo.getThread();
        likeMapper.delete(articleId,userId);
        articleMapper.likeOrCollect(articleId,OtherConstants.likes,OtherConstants.delete);
        return Result.success();
    }

    @Override
    public void comment(CommentDTO commentDTO) {
        Long userId = ThreadInfo.getThread();
        Comments comment = new Comments();
        BeanUtils.copyProperties(commentDTO,comment);
        comment.setUserId(userId);
        String userName = userMapper.getNickNameById(userId);
        comment.setUserNickname(userName);
        commentMapper.insert(comment);
    }


    private boolean judgePhoneNumber(String phoneNumber){
            return Pattern.matches(OtherConstants.CHINA_PHONE_NUMBER_REGEX, phoneNumber);
    }

    private String randomString(){
        Random random = new Random();
        Integer randomInt = 100000+ random.nextInt(999999);
        return String.valueOf(randomInt);
    }
}
