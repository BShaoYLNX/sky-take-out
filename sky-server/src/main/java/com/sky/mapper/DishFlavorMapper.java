package com.sky.mapper;

import com.sky.entity.DishFlavor;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
@Mapper
public interface DishFlavorMapper {
    /**
     * 批量插入口味
     */
    void insertBatch(@Param("list") List<DishFlavor> flavors);

    /**
     * 根据菜品ID删除所有口味
     */
    void deleteByDishId(Long dishId);

    /**
     * 根据菜品ID查询口味列表
     */
    List<DishFlavor> getByDishId(Long dishId);
}
