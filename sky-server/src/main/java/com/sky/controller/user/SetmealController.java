package com.sky.controller.user;

import com.sky.dto.SetmealDTO;
import com.sky.dto.SetmealPageQueryDTO;
import com.sky.entity.Setmeal;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.SetmealService;
import com.sky.vo.SetmealVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController("SetmealUserController")
@RequestMapping("/user/setmeal")
@Api(tags = "用户端-套餐接口")
@Slf4j
public class SetmealController {

    @Autowired
    private SetmealService setmealService;

    @GetMapping("/page")
    @ApiOperation("套餐分页查询")
    public Result<PageResult> page(SetmealPageQueryDTO pageQueryDTO) {
        log.info("用户端套餐分页查询：{}", pageQueryDTO);
        PageResult pageResult = setmealService.pageQuery(pageQueryDTO);
        return Result.success(pageResult);
    }

    @GetMapping("/{id}")
    @ApiOperation("根据ID查询套餐详情（含关联菜品）")
    public Result<SetmealDTO> getById(@PathVariable Long id) {
        log.info("查询套餐详情，id={}", id);
        SetmealDTO dto = setmealService.getByIdWithDishes(id);
        return Result.success(dto);
    }
}