package com.sky.mapper;

import com.github.pagehelper.Page;
import com.sky.dto.OrdersPageQueryDTO;
import com.sky.entity.Orders;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDateTime;
import java.util.List;

@Mapper
public interface OrderMapper {
    /**
     * 插入订单
     */
    void insert(Orders orders);

    Orders getByNumber(@Param("number") String number);

    Orders getById(@Param("id") Long id);

    /**
     * 分页查询
     */
    Page<Orders> pageQuery(OrdersPageQueryDTO dto);

    /**
     * 动态更新订单
     */
    void update(Orders orders);

    /**
     * 统计某状态订单数量
     */
    Integer countStatus(@Param("status") Integer status);

    /**
     * 查询超时未支付订单
     */
    List<Orders> getByStatusAndOrderTime(@Param("status") Integer status,
                                         @Param("time") LocalDateTime time);
}