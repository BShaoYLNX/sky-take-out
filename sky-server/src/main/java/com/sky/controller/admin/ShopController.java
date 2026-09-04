package com.sky.controller.admin;

import com.sky.constant.MessageConstant;
import com.sky.result.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

/**
 * 店铺相关接口操作
 */
@RestController("ShopAdminController")
@RequestMapping( "/admin/shop")
@Slf4j
@Api(tags = "店铺相关接口操作")
public class ShopController {
    //设置redis key常量
    public static final String KEY = "SHOP_STATUS";
    @Autowired
    private RedisTemplate redisTemplate;
    /**
     * 设置营业状态
     * @param status
     * @return
     */
    @PutMapping("/{status}")
    @ApiOperation("设置营业状态")
    public Result SetStatus(@PathVariable Integer status){
        log.info("设置营业状态为: {}",status == 1 ? "营业中":"打样中");
        redisTemplate.opsForValue().set(KEY,String.valueOf(status));
        return Result.success();
    }
    /**
     * 获取店铺营业状态
     * @return
     */
    @GetMapping("/status")
    @ApiOperation("获取店铺营业状态")
    public Result<Integer> getStatus() {
        // 从 Redis 读取状态
        String statusStr = (String) redisTemplate.opsForValue().get(KEY);
        Integer status = null;
        status = Integer.valueOf(statusStr);
        log.info("获取店铺营业状态：{}", status == 1 ? "营业中" : "打烊中");
        return Result.success(status);
    }
}
