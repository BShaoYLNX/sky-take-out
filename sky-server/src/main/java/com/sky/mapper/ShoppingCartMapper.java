package com.sky.mapper;

import com.sky.entity.ShoppingCart;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface ShoppingCartMapper {
    /**
     * 根据用户ID查询购物车
     */
    List<ShoppingCart> listByUserId(@Param("userId") Long userId);
}