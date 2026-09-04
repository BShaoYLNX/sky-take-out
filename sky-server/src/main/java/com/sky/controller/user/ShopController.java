package com.sky.controller.user;

import com.sky.result.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController("ShopUserController")
@RequestMapping("/user/shop")
@Slf4j
@Api(tags = "用户端店铺相关接口")
public class ShopController {

    public static final String KEY = "SHOP_STATUS";

    @Autowired
    private RedisTemplate redisTemplate;

    @GetMapping("/status")
    @ApiOperation("获取店铺营业状态")
    public Result<Integer> getStatus() {
        // 从 Redis 读取状态
        String statusStr = (String) redisTemplate.opsForValue().get(KEY);
        Integer status = null;
        if (statusStr != null) {
            try {
                status = Integer.valueOf(statusStr);
            } catch (NumberFormatException e) {
                log.warn("Redis 中存储的状态值不是数字: {}", statusStr);
            }
        }
        log.info("获取店铺营业状态：{}", status == null ? "未知" : (status == 1 ? "营业中" : "打烊中"));
        return Result.success(status);
    }
}