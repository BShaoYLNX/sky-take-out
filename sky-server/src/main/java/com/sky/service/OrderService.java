package com.sky.service;

import com.sky.dto.*;
import com.sky.result.PageResult;
import com.sky.vo.OrderPaymentVO;
import com.sky.vo.OrderStatisticsVO;
import com.sky.vo.OrderSubmitVO;
import com.sky.vo.OrderVO;

public interface OrderService {

    /**
     * 用户下单
     */
    OrderSubmitVO submitOrder(OrdersSubmitDTO ordersSubmitDTO);

    /**
     * 订单分页查询
     */
    PageResult conditionSearch(OrdersPageQueryDTO dto);

    /**
     * 各状态订单数量统计
     */
    OrderStatisticsVO statistics();

    /**
     * 查询订单详情
     */
    OrderVO details(Long id);

    /**
     * 接单
     */
    void confirm(OrdersConfirmDTO dto);

    /**
     * 拒单
     */
    void rejection(OrdersRejectionDTO dto);

    /**
     * 取消订单
     */
    void cancel(OrdersCancelDTO dto);

    /**
     * 派送订单
     */
    void delivery(Long id);

    /**
     * 完成订单
     */
    void complete(Long id);
    /**
     * 微信支付
     */
    OrderPaymentVO payment(OrdersPaymentDTO ordersPaymentDTO);
}