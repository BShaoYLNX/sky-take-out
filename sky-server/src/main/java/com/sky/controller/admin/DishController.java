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
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@RestController("DishAdminController")
@RequestMapping("/admin/dish")
@Api(tags = "菜品管理")
@Slf4j
public class DishController {

    @Autowired
    private DishService dishService;

    @Autowired
    private RedisTemplate redisTemplate;

    /**
     * 清理所有菜品缓存（key 以 dish_ 开头）
     */
    private void clearDishCache() {
        Set keys = redisTemplate.keys("dish_*");
        if (keys != null && !keys.isEmpty()) {
            redisTemplate.delete(keys);
            log.info("已清理菜品缓存，共 {} 个 key", keys.size());
        }
    }

    /**
     * 新增菜品
     */
    @PostMapping
    @ApiOperation("新增菜品")
    public Result addDish(@RequestBody DishDTO dishDTO) {
        log.info("新增菜品：{}", dishDTO);
        dishService.addDish(dishDTO);
        // 清理缓存
        clearDishCache();
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
     * 根据ID查询菜品详情
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
        // 清理缓存
        clearDishCache();
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

//    /**
//     * 起售、停售菜品
//     */
//    @PostMapping("/status/{status}")
//    @ApiOperation("起售停售菜品")
//    public Result startOrStop(@PathVariable Integer status, @RequestParam Long id) {
//        log.info("起售停售菜品：id={}, status={}", id, status);
//        dishService.startOrStop(status, id);
//        clearDishCache();   // 清理缓存
//        return Result.success();
//    }
}