package com.sky.service;

import com.sky.dto.ShoppingCartDTO;
import com.sky.entity.ShoppingCart;

import java.util.List;

/**
 *购物车服务层接口
 */
public interface ShoppingCartService {
    /**
     * 查询当前用户的购物车列表
     */
    List<ShoppingCart> list();

    /**
     * 添加购物车
     * @param shoppingCartDTO
     */
    void addShoppingCart(ShoppingCartDTO shoppingCartDTO);
    /**
     * 清空当前用户的购物车
     */
    void clean();
    /**
     * 删除购物车中一个商品
     */
    void subShoppingCart(ShoppingCartDTO shoppingCartDTO);
}