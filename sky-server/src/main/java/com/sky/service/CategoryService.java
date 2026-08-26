package com.sky.service;

import com.sky.dto.CategoryDTO;
import com.sky.dto.CategoryPageQueryDTO;
import com.sky.entity.Category;
import com.sky.result.PageResult;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 分类查询接口
 */
public interface CategoryService {
    /**
     * 分页查询
     * @param categoryPageQueryDTO
     * @return
     */
    PageResult pageQuery(CategoryPageQueryDTO categoryPageQueryDTO);

    /**
     * 新增分类
     * @param categoryDTO
     */
    void addCategory(CategoryDTO categoryDTO);

    /**
     * 修改操作
     * @param categoryDTO
     */
    void updateCategory(CategoryDTO categoryDTO);

    /**
     * 删除操作
     * @param id
     */
    void deleteCategory(Long id);

    /**
     * 根据类型查询分类列表
     * @param type 类型：1-菜品分类，2-套餐分类
     * @return 分类列表
     */
    List<Category> listByType(Integer type);
}
