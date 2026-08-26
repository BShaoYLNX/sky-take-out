package com.sky.service;

import com.sky.dto.DishDTO;
import com.sky.dto.DishPageQueryDTO;
import com.sky.entity.Dish;
import com.sky.result.PageResult;
import com.sky.vo.DishVO;

import java.util.List;

public interface DishService {
    /**
     * 新增菜品（含口味）
     */
    void addDish(DishDTO dishDTO);

    /**
     * 分页查询
     */
    PageResult pageQuery(DishPageQueryDTO pageQueryDTO);

    /**
     * 根据ID查询菜品详情
     */
    DishVO getByIdWithFlavor(Long id);

    /**
     * 修改菜品（更新基本信息和口味）
     */
    void updateDish(DishDTO dishDTO);

    /**
     * 根据分类ID查询菜品列表
     */
    List<Dish> getByCategoryId(Long categoryId);
}
