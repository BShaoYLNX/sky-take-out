package com.sky.service;

import com.sky.dto.SetmealDTO;
import com.sky.dto.SetmealPageQueryDTO;
import com.sky.result.PageResult;

public interface SetmealService {
    //分页查询
    PageResult pageQuery(SetmealPageQueryDTO pageQueryDTO);
    //新增套餐
    void addSetmeal(SetmealDTO setmealDTO);
}
