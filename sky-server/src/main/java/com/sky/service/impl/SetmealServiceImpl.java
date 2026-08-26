package com.sky.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sky.context.BaseContext;
import com.sky.controller.admin.SetmealController;
import com.sky.dto.SetmealDTO;
import com.sky.dto.SetmealPageQueryDTO;
import com.sky.entity.Dish;
import com.sky.entity.Setmeal;
import com.sky.entity.SetmealDish;
import com.sky.mapper.DishMapper;
import com.sky.mapper.SetmealDishMapper;
import com.sky.mapper.SetmealMapper;
import com.sky.result.PageResult;
import com.sky.service.SetmealService;
import com.sky.vo.SetmealVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

@Service
public class SetmealServiceImpl implements SetmealService {

    @Autowired
    private SetmealMapper setmealMapper;

    @Autowired
    private SetmealDishMapper setmealDishMapper;

    @Autowired
    private DishMapper dishMapper;
    @Override
    @Transactional
    public void addSetmeal(SetmealDTO setmealDTO) {
        // 套餐基本信息
        Setmeal setmeal = new Setmeal();
        BeanUtils.copyProperties(setmealDTO, setmeal);
        // 默认起售
        setmeal.setStatus(1);
        setmeal.setCreateTime(LocalDateTime.now());
        setmeal.setUpdateTime(LocalDateTime.now());
        setmeal.setCreateUser(BaseContext.getCurrentId());
        setmeal.setUpdateUser(BaseContext.getCurrentId());

        //插入套餐，获取id
        setmealMapper.insert(setmeal);
        Long setmealId = setmeal.getId();

        //处理关联菜品
        List<SetmealDish> dishList = setmealDTO.getSetmealDishes();
        if (!CollectionUtils.isEmpty(dishList)) {
            //为每个关联菜品设置套餐id
            for (SetmealDish dish : dishList) {
                dish.setSetmealId(setmealId);
            }
            // 批量插入
            setmealDishMapper.insertBatch(dishList);
        }
    }

    @Override
    public PageResult pageQuery(SetmealPageQueryDTO pageQueryDTO) {
        PageHelper.startPage(pageQueryDTO.getPage(), pageQueryDTO.getPageSize());
        List<SetmealVO> list = setmealMapper.pageQuery(
                pageQueryDTO.getName(),
                pageQueryDTO.getCategoryId(),
                pageQueryDTO.getStatus()
        );
        Page<SetmealVO> page = (Page<SetmealVO>) list;
        return new PageResult(page.getTotal(), page.getResult());
    }

    @Override
    public SetmealDTO getByIdWithDishes(Long id) {
        //查询套餐基本信息
        Setmeal setmeal = setmealMapper.getById(id);
        if (setmeal == null) {
            throw new RuntimeException("套餐不存在");
        }

        // 查询关联菜品
        List<SetmealDish> dishList = setmealDishMapper.getBySetmealId(id);

        // 组装DTO
        SetmealDTO dto = new SetmealDTO();
        BeanUtils.copyProperties(setmeal, dto);
        dto.setSetmealDishes(dishList);
        return dto;
    }
    @Override
    @Transactional
    public void updateSetmeal(SetmealDTO setmealDTO) {
        // 校验id
        if (setmealDTO.getId() == null) {
            throw new RuntimeException("套餐ID不能为空");
        }

        // 更新基本信息
        Setmeal setmeal = new Setmeal();
        BeanUtils.copyProperties(setmealDTO, setmeal);
        setmeal.setUpdateTime(LocalDateTime.now());
        setmeal.setUpdateUser(BaseContext.getCurrentId());
        setmealMapper.update(setmeal);

        Long setmealId = setmealDTO.getId();
        // 先删后插关联菜品
        setmealDishMapper.deleteBySetmealIds(Collections.singletonList(setmealId));
        List<SetmealDish> dishList = setmealDTO.getSetmealDishes();
        if (!CollectionUtils.isEmpty(dishList)) {
            for (SetmealDish dish : dishList) {
                dish.setSetmealId(setmealId);
                if (dish.getName() == null || dish.getPrice() == null) {
                    Dish dishInfo = dishMapper.getById(dish.getDishId());
                    if (dishInfo != null) {
                        dish.setName(dishInfo.getName());
                        dish.setPrice(dishInfo.getPrice());
                    }
                }
            }
            setmealDishMapper.insertBatch(dishList);
        }
    }

    @Override
    @Transactional
    public void deleteSetmeal(List<Long> ids) {
        if (CollectionUtils.isEmpty(ids)) {
            throw new RuntimeException("请选择要删除的套餐");
        }

        // 检查是否有套餐在起售状态
        for (Long id : ids) {
            Setmeal setmeal = setmealMapper.getById(id);
            if (setmeal == null) {
                throw new RuntimeException("套餐ID " + id + " 不存在");
            }
            if (setmeal.getStatus() == 1) {
                throw new RuntimeException("套餐[" + setmeal.getName() + "]正在起售中，不能删除");
            }
        }
        // 批量删除套餐
        setmealMapper.deleteBatch(ids);
        // 批量删除关联菜品
        setmealDishMapper.deleteBySetmealIds(ids);
    }

    @Override
    public void startOrStop(Long id, Integer status) {
        Setmeal setmeal = new Setmeal();
        setmeal.setId(id);
        setmeal.setStatus(status);
        setmeal.setUpdateTime(LocalDateTime.now());
        setmeal.setUpdateUser(BaseContext.getCurrentId());
        setmealMapper.update(setmeal);
    }
}