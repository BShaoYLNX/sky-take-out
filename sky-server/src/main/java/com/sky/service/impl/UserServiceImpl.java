package com.sky.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.sky.constant.MessageConstant;
import com.sky.dto.UserLoginDTO;
import com.sky.entity.User;
import com.sky.exception.UserNotLoginException;
import com.sky.mapper.UserMapper;
import com.sky.properties.WeChatProperties;
import com.sky.service.UserService;
import com.sky.utils.HttpClientUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.net.URL;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * 用户服务接口实现类
 */
@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private WeChatProperties weChatProperties;
    @Autowired
    private UserMapper userMapper;
    /**
     * url常量
     */
    public static final String URL = "https://api.weixin.qq.com/sns/jscode2session";
    /**
     * 微信登陆
     * @param userLoginDTO
     * @return
     */
    @Override
    public User wxLogin(UserLoginDTO userLoginDTO) {
        //根据code获得当前openid
        //拼接url
        //吧需要的属性存入Map里面
        Map<String,String> map = new HashMap<>();
        map.put("appid",weChatProperties.getAppid());
        map.put("secret",weChatProperties.getSecret());
        map.put("js_code",userLoginDTO.getCode());
        map.put("grant_type","authorization_code");
        //向微信发送登录请求拿到openid
        String json = HttpClientUtil.doGet(URL,map);
        //格式转化
        JSONObject jSONObject = JSONObject.parseObject(json);
        //拿到openid
        String openId = jSONObject.getString("openid");
        //判断数据库里面有没有该用户也就是有没有该openid没有的话就注册
        if(openId== null){
        throw new UserNotLoginException(MessageConstant.LOGIN_FAILED);
        }
        //在判断当前的断此openid在数据库中是否存在
        User user = userMapper.selectUserInfoByOpenId(openId);
        if(user == null){
            //代表该用户未注册
            user = User.builder().openid(openId).createTime(LocalDateTime.now()).build();
            //新增数据
            userMapper.insert(user);

        }
        return user;
    }
}
