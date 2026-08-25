package com.sky.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sky.context.BaseContext;
import com.sky.dto.DishDTO;
import com.sky.dto.DishPageQueryDTO;
import com.sky.entity.Dish;
import com.sky.entity.DishFlavor;
import com.sky.mapper.DishFlavorMapper;
import com.sky.mapper.DishMapper;
import com.sky.result.PageResult;
import com.sky.service.DishService;
import com.sky.vo.DishVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class DishServiceImpl implements DishService {
    @Autowired
    private DishMapper dishMapper;

    @Autowired
    private DishFlavorMapper dishFlavorMapper;

    @Override
    @Transactional
    public void addDish(DishDTO dishDTO) {
        //DTO 转 Entity（菜品基本信息）
        Dish dish = new Dish();
        BeanUtils.copyProperties(dishDTO, dish);

        //补全系统字段
        dish.setStatus(1);
        dish.setCreateTime(LocalDateTime.now());
        dish.setUpdateTime(LocalDateTime.now());
        dish.setCreateUser(BaseContext.getCurrentId());
        dish.setUpdateUser(BaseContext.getCurrentId());

        //插入菜品，获取 id
        dishMapper.insert(dish);
        Long dishId = dish.getId();

        //  处理口味列表
        List<DishFlavor> flavors = dishDTO.getFlavors();
        if (flavors != null && !flavors.isEmpty()) {
            // 设置菜品 id
            flavors.forEach(flavor -> flavor.setDishId(dishId));
            // 批量插入
            dishFlavorMapper.insertBatch(flavors);
        }
    }

    @Override
    public PageResult pageQuery(DishPageQueryDTO pageQueryDTO) {
        // 分页
        PageHelper.startPage(pageQueryDTO.getPage(), pageQueryDTO.getPageSize());

        // 查询
        List<DishVO> list = dishMapper.pageQuery(
                pageQueryDTO.getName(),
                pageQueryDTO.getCategoryId(),
                pageQueryDTO.getStatus()
        );

        // 封装结果
        Page<DishVO> page = (Page<DishVO>) list;
        return new PageResult(page.getTotal(), page.getResult());
    }
}
