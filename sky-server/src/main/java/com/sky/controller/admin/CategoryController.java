package com.sky.controller.admin;

import com.sky.dto.CategoryDTO;
import com.sky.dto.CategoryPageQueryDTO;
import com.sky.entity.Category;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.CategoryService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin/category")
@Slf4j
@Api(tags = "分页查询操作")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    @ApiOperation("员工分页查询")
    @GetMapping("/page")
    public Result<PageResult> page(CategoryPageQueryDTO categoryPageQueryDTO){
        //@Slf4j中的方法可以在日志中打印实体类
        log.info("分页查询的实体类信息 {}" , categoryPageQueryDTO);
        //实现分页查询
        PageResult pageResult = categoryService.pageQuery(categoryPageQueryDTO);
        //返回分页查询后的对象
        return Result.success(pageResult);
    }

    /**
     * 新增套餐或者菜单方法
     * @param CategoryDTO
     * @return
     */
    @PostMapping
    @ApiOperation("新增套餐/菜单")
    public Result addCategory(@RequestBody CategoryDTO CategoryDTO){
        log.info("新增分类内容 {}", CategoryDTO);
        //实现新增分类内容
        categoryService.addCategory(CategoryDTO);
        return Result.success();
    }
    /**
     * 修改分类
     * @param categoryDTO 包含id及需要修改的字段
     * @return 操作结果
     */
    @PutMapping
    @ApiOperation("修改分类")
    public Result updateCategory(@RequestBody CategoryDTO categoryDTO) {
        log.info("修改分类内容：{}", categoryDTO);
        categoryService.updateCategory(categoryDTO);
        return Result.success();
    }

    /**
     * 删除分类（根据ID）
     * @param id 分类ID
     * @return 操作结果
     */
    @DeleteMapping
    @ApiOperation("删除分类")
    public Result deleteCategory(@RequestParam Long id) {
        log.info("删除分类ID：{}", id);
        categoryService.deleteCategory(id);
        return Result.success();
    }

    /**
     * 根据类型查询分类
     * @param type 类型：1-菜品分类，2-套餐分类
     * @return 分类列表
     */
    @GetMapping("/list")
    @ApiOperation("根据类型查询分类")
    public Result<List<Category>> listByType(@RequestParam(required = false) Integer type) {
        log.info("根据类型查询分类，type={}", type);
        List<Category> list = categoryService.listByType(type);
        return Result.success(list);
    }
}
