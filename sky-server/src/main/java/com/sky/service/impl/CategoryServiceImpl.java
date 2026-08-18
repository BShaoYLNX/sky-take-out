package com.sky.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sky.context.BaseContext;
import com.sky.dto.CategoryDTO;
import com.sky.dto.CategoryPageQueryDTO;
import com.sky.entity.Category;
import com.sky.mapper.CategoryMapper;
import com.sky.result.PageResult;
import com.sky.service.CategoryService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PostMapping;

import java.time.LocalDateTime;

/**
 * 分类分页操作实现类
 */

@Service
public class CategoryServiceImpl implements CategoryService{
    @Autowired
    CategoryMapper categoryMapper;
    /**
     * 分页查询
     * @param categoryPageQueryDTO
     * @return
     */
    @Override
    public PageResult pageQuery(CategoryPageQueryDTO categoryPageQueryDTO) {
        //进行分页查询
        //分页查询步骤
        //PageHelper是分页查询插件 先获取当前页数为起始页码和一页记录数
        PageHelper.startPage(categoryPageQueryDTO.getPage(),categoryPageQueryDTO.getPageSize());
        Page<Category> pageCategory = categoryMapper.pageQuery(categoryPageQueryDTO);
        //固定返回值写法
        //pageCategory.getTotal()当前页码 pageCategory.getResult()返回数据
        return new PageResult(pageCategory.getTotal(),pageCategory.getResult());
    }

    @Override
    public void addCategory(CategoryDTO categoryDTO) {
        //DTO里面内容是用来接收前端传来的数据的所以说需要转换一下
        Category category = new Category();
        //BeanUtils类中的copyProperties会把categoryDTO的值赋值给category
        BeanUtils.copyProperties(categoryDTO,category);
        //由于DTO里面的内容少赋值过去会导致category里面会缺少几个值
        //所以说手动添加进去
        //状态 分类的初始状态（为1可以使用） 创建时间 创建人 修改时间 修改人
        category.setStatus(1);
        category.setCreateTime(LocalDateTime.now());
        category.setCreateUser(BaseContext.getCurrentId());
        category.setUpdateTime(LocalDateTime.now());
        category.setUpdateUser(BaseContext.getCurrentId());
        //实现新增方法
        categoryMapper.addCategory(category);
    }

    /**
     * 修改操作
     * @param categoryDTO
     */
    @Override
    public void updateCategory(CategoryDTO categoryDTO) {
        // 确保传入的DTO包含id
        if (categoryDTO.getId() == null) {
            throw new RuntimeException("分类ID不能为空");
        }
        // 将DTO转成Entity
        Category category = new Category();
        BeanUtils.copyProperties(categoryDTO, category);
        // 手动设置更新时间、更新人
        category.setUpdateTime(LocalDateTime.now());
        category.setUpdateUser(BaseContext.getCurrentId());
        // 调用Mapper更新
        categoryMapper.updateCategory(category);
    }

    /**
     * 删除操作
     * @param id
     */
    @Override
    public void deleteCategory(Long id) {
        categoryMapper.deleteCategory(id);
    }
}
