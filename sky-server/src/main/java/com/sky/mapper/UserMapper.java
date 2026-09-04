package com.sky.mapper;

import com.sky.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

/**
 * 用户数据层接口
 */
@Mapper
public interface UserMapper {
    /**
     * 通过openid获取用户信息
     * @param openId
     * @return
     */
    @Select("select * from [user] where openid = #{openId}")
    User selectUserInfoByOpenId(String openId);

    /**
     * 新增数据
     * @param user
     */
    void insert(User user);
}
