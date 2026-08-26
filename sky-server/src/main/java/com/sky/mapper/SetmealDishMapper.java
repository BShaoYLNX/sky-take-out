package com.sky.mapper;

import com.sky.entity.SetmealDish;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface SetmealDishMapper {
    /**
     * 批量插入套餐菜品关系
     */
    void insertBatch(@Param("list") List<SetmealDish> list);

    /**
     * 根据套餐ID列表批量删除关联菜品
     */
    void deleteBySetmealIds(@Param("ids") List<Long> ids);

    /**
     * 根据套餐ID查询关联菜品
     */
    List<SetmealDish> getBySetmealId(@Param("setmealId") Long setmealId);
}
