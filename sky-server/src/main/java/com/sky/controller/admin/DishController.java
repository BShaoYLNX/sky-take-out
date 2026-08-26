package com.sky.controller.admin;

import com.sky.dto.DishDTO;
import com.sky.dto.DishPageQueryDTO;
import com.sky.entity.Dish;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.DishService;
import com.sky.vo.DishVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin/dish")
@Api(tags = "菜品管理")
@Slf4j
public class DishController {
    @Autowired
    private DishService dishService;

    /**
     * 新增菜品
     */
    @PostMapping
    @ApiOperation("新增菜品")
    public Result addDish(@RequestBody DishDTO dishDTO) {
        log.info("新增菜品：{}", dishDTO);
        dishService.addDish(dishDTO);
        return Result.success();
    }

    /**
     * 菜品分页查询
     */
    @GetMapping("/page")
    @ApiOperation("菜品分页查询")
    public Result<PageResult> pageQuery(DishPageQueryDTO pageQueryDTO) {
        log.info("菜品分页查询：{}", pageQueryDTO);
        PageResult pageResult = dishService.pageQuery(pageQueryDTO);
        return Result.success(pageResult);
    }
    /**
     * 根据ID查询菜品详情（回显）
     */
    @GetMapping("/{id}")
    @ApiOperation("根据ID查询菜品")
    public Result<DishVO> getById(@PathVariable Long id) {
        log.info("查询菜品详情，id={}", id);
        DishVO vo = dishService.getByIdWithFlavor(id);
        return Result.success(vo);
    }

    /**
     * 修改菜品
     */
    @PutMapping
    @ApiOperation("修改菜品")
    public Result updateDish(@RequestBody DishDTO dishDTO) {
        log.info("修改菜品：{}", dishDTO);
        dishService.updateDish(dishDTO);
        return Result.success();
    }

    /**
     * 根据分类ID查询菜品
     */
    @GetMapping("/list")
    @ApiOperation("根据分类ID查询菜品")
    public Result<List<Dish>> getByCategoryId(@RequestParam Long categoryId) {
        log.info("根据分类ID查询菜品，categoryId={}", categoryId);
        List<Dish> list = dishService.getByCategoryId(categoryId);
        return Result.success(list);
    }
}
