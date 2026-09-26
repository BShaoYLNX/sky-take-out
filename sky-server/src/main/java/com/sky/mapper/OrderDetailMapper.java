package com.sky.mapper;

import com.sky.entity.OrderDetail;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface OrderDetailMapper {

    /**
     *  批量插入订单明细
     */
    void insertBatch(@Param("list") List<OrderDetail> list);
    /**
     * 根据订单id查询明细
     */
    List<OrderDetail> getByOrderId(@Param("orderId") Long orderId);
}