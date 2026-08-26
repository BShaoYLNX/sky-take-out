package com.sky.mapper;


import com.github.pagehelper.Page;
import com.sky.dto.CategoryPageQueryDTO;
import com.sky.entity.Category;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Update;

import java.util.List;

@Mapper
public interface CategoryMapper {
    /**
     * 新增分类
     * @param categoryPageQueryDTO
     * @return
     */
    Page<Category> pageQuery(CategoryPageQueryDTO categoryPageQueryDTO);
    @Insert("INSERT INTO category (type, name, sort, status, create_time, update_time, create_user, update_user) " +
            "VALUES (#{type}, #{name}, #{sort}, #{status}, #{createTime}, #{updateTime}, #{createUser}, #{updateUser})")
    void addCategory(Category category);

    /**
     * 更新分类
     * @param category
     */
    @Update("UPDATE category SET " +
            "type = #{type}, " +
            "name = #{name}, " +
            "sort = #{sort}, " +
            "update_time = #{updateTime}, " +
            "update_user = #{updateUser} " +
            "WHERE id = #{id}")
    void updateCategory(Category category);

    /**
     * 删除分类
     * @param id
     */
    @Delete("DELETE FROM category WHERE id = #{id}")
    void deleteCategory(Long id);
    /**
     * 根据类型查询分类列表
     * @param type 类型：1-菜品分类，2-套餐分类
     * @return 分类列表
     */
    List<Category> listByType(Integer type);
}
