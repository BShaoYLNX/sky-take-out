package com.sky.controller.admin;

import com.sky.dto.SetmealDTO;
import com.sky.dto.SetmealPageQueryDTO;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.SetmealService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.web.bind.annotation.*;
import org.springframework.cache.annotation.Cacheable;

import java.util.List;

/**
 * 套餐管理
 */
@RestController
@RequestMapping("/admin/setmeal")
@Api(tags = "套餐管理")
@Slf4j
public class SetmealController {
    @Autowired
    private SetmealService setmealService;

    @PostMapping
    @ApiOperation("新增套餐")
    @CacheEvict(cacheNames = "setMealCache", key = "#setmealDTO.categoryId")
    public Result addSetmeal(@RequestBody SetmealDTO setmealDTO) {
        log.info("新增套餐：{}", setmealDTO);
        setmealService.addSetmeal(setmealDTO);
        return Result.success();
    }

    @GetMapping("/page")
    @ApiOperation("套餐分页查询")
    public Result<PageResult> pageQuery(SetmealPageQueryDTO pageQueryDTO) {
        log.info("套餐分页查询：{}", pageQueryDTO);
        PageResult pageResult = setmealService.pageQuery(pageQueryDTO);
        return Result.success(pageResult);
    }

    @GetMapping("/{id}")
    @ApiOperation("根据ID查询套餐详情")
    public Result<SetmealDTO> getById(@PathVariable Long id) {
        log.info("查询套餐详情，id={}", id);
        SetmealDTO dto = setmealService.getByIdWithDishes(id);
        return Result.success(dto);
    }

    @PutMapping
    @ApiOperation("修改套餐")
    @CacheEvict(cacheNames = "setMealCache", key = "#setmealDTO.categoryId")
    public Result updateSetmeal(@RequestBody SetmealDTO setmealDTO) {
        log.info("修改套餐：{}", setmealDTO);
        setmealService.updateSetmeal(setmealDTO);
        return Result.success();
    }

    /**
     * 删除套餐
     * @param
     * @return
     */
    @DeleteMapping
    @ApiOperation("删除套餐")
    @CacheEvict(cacheNames = "setMealCache", key = "#setmealDTO.categoryId")
    public Result deleteSetmeal(@RequestParam List<Long> ids) {
        log.info("删除套餐，ids={}", ids);
        setmealService.deleteSetmeal(ids);
        return Result.success();
    }

    @PostMapping("/status/{status}")
    @ApiOperation("启停售套餐")
    @CacheEvict(cacheNames = "setMealCache", key = "#setmealDTO.categoryId")
    public Result startOrStop(@PathVariable Integer status, @RequestParam Long id) {
        log.info("启停售套餐，id={}, status={}", id, status);
        setmealService.startOrStop(id, status);
        return Result.success();
    }
}