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
}
