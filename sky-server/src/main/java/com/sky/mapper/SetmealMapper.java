package com.sky.mapper;

import com.sky.entity.Dish;
import com.sky.entity.Setmeal;
import com.sky.vo.SetmealVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface SetmealMapper {

    /**
     * 新增套餐
     */
    void insert(Setmeal setmeal);

    /**
     * 分页查询
     */
    List<SetmealVO> pageQuery(@Param("name") String name,
                              @Param("categoryId") Long categoryId,
                              @Param("status") Integer status);

    /**
     * 根据ID查询套餐
     */
    Setmeal getById(@Param("id") Long id);

    /**
     * 修改套餐
     */
    void update(Setmeal setmeal);

    /**
     * 批量删除套餐
     */
    void deleteBatch(@Param("ids") List<Long> ids);
}
