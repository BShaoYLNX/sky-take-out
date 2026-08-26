package com.sky.service;

import com.sky.dto.SetmealDTO;
import com.sky.dto.SetmealPageQueryDTO;
import com.sky.result.PageResult;
import com.sky.vo.DishVO;

import java.util.List;

public interface SetmealService {
    /**
     * 新增套餐
     */
    void addSetmeal(SetmealDTO setmealDTO);

    /**
     * 分页查询
     */
    PageResult pageQuery(SetmealPageQueryDTO pageQueryDTO);

    /**
     * 根据ID查询套餐详情（含关联菜品）
     */
    SetmealDTO getByIdWithDishes(Long id);

    /**
     * 修改套餐
     */
    void updateSetmeal(SetmealDTO setmealDTO);

    /**
     * 删除套餐
     */
    void deleteSetmeal(List<Long> ids);

    /**
     * 启停售套餐
     */
    void startOrStop(Long id, Integer status);
}
