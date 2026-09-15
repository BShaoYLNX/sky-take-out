package com.sky.controller.user;

import com.alibaba.fastjson.JSON;
import com.sky.entity.Dish;
import com.sky.result.Result;
import com.sky.service.DishService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController("DishUserController")
@RequestMapping("/user/dish")
@Api(tags = "用户端-菜品接口")
@Slf4j
public class DishController {

    @Autowired
    private DishService dishService;

    @Autowired
    private RedisTemplate redisTemplate;

    @GetMapping("/list")
    @ApiOperation("根据分类ID查询菜品（起售中）")
    public Result<List<Dish>> list(@RequestParam Long categoryId) {
        log.info("用户端查询菜品，categoryId={}", categoryId);

        //拼接缓存key
        String key = "dish_" + categoryId;

        //先从缓存读取
        String cachedJson = (String) redisTemplate.opsForValue().get(key);
        if (cachedJson != null && !cachedJson.isEmpty()) {
            //反序列化为 List<Dish>
            List<Dish> dishList = JSON.parseArray(cachedJson, Dish.class);
            log.info("从缓存中读取菜品，categoryId={}", categoryId);
            return Result.success(dishList);
        }

        // 缓存不存在，查询数据库
        List<Dish> list = dishService.getByCategoryId(categoryId);

        // 存入缓存（JSON 字符串）
        redisTemplate.opsForValue().set(key, JSON.toJSONString(list));

        return Result.success(list);
    }
}