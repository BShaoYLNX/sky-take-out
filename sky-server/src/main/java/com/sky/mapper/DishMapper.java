package com.sky.mapper;

import com.sky.entity.Dish;
import com.sky.vo.DishVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface DishMapper {
    /**
     * 新增菜品
     */
    void insert(Dish dish);

    /**
     * 分页查询（含分类名称）
     */
    List<DishVO> pageQuery(@Param("name") String name,
                           @Param("categoryId") Integer categoryId,
                           @Param("status") Integer status);

    /**
     * 根据ID查询菜品
     */
    Dish getById(Long id);

    /**
     * 更新菜品（动态更新）
     */
    void update(Dish dish);

    /**
     * 根据分类ID查询菜品列表
     */
    List<Dish> getByCategoryId(Long categoryId);
}
