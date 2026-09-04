package com.sky.service;

import com.sky.dto.UserLoginDTO;
import com.sky.entity.User;

/**
 * 用户服务接口
 */
public interface UserService {
    //微信登陆
    User wxLogin(UserLoginDTO userLoginDTO);
}
