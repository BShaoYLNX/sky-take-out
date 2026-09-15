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

    /**
     * 获取当前所有购物车信息
     * @param userId
     */
    List<ShoppingCart> selcetShoppingCarByUserId(Long userId);
    /**
     *  新增购物车记录
     *  @param shoppingCart
     */
    void insert(ShoppingCart shoppingCart);

    /**
     * 更新购物车
     * @param shoppingCart
     */
    void updateNumberById(ShoppingCart shoppingCart);
    /**
     * 根据用户id删除购物车中所有记录
     */
    void deleteByUserId(@Param("userId") Long userId);
    /**
     * 根据id删除购物车记录
     */
    void deleteById(@Param("id") Long id);
}