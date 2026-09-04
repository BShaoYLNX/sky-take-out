package com.sky.service;

import com.sky.entity.ShoppingCart;

import java.util.List;

public interface ShoppingCartService {
    /**
     * 查询当前用户的购物车列表
     */
    List<ShoppingCart> list();
}