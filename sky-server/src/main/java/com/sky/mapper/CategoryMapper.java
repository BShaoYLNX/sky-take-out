package com.sky.mapper;


import com.github.pagehelper.Page;
import com.sky.dto.CategoryPageQueryDTO;
import com.sky.entity.Category;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Update;

@Mapper
public interface CategoryMapper {

    Page<Category> pageQuery(CategoryPageQueryDTO categoryPageQueryDTO);
    @Insert("INSERT INTO category (type, name, sort, status, create_time, update_time, create_user, update_user) " +
            "VALUES (#{type}, #{name}, #{sort}, #{status}, #{createTime}, #{updateTime}, #{createUser}, #{updateUser})")
    void addCategory(Category category);
    @Update("UPDATE category SET " +
            "type = #{type}, " +
            "name = #{name}, " +
            "sort = #{sort}, " +
            "update_time = #{updateTime}, " +
            "update_user = #{updateUser} " +
            "WHERE id = #{id}")
    void updateCategory(Category category);
    @Delete("DELETE FROM category WHERE id = #{id}")
    void deleteCategory(Long id);
}
