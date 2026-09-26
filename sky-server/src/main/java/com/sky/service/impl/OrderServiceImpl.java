package com.sky.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sky.context.BaseContext;
import com.sky.dto.*;
import com.sky.entity.AddressBook;
import com.sky.entity.OrderDetail;
import com.sky.entity.Orders;
import com.sky.entity.ShoppingCart;
import com.sky.mapper.AddressBookMapper;
import com.sky.mapper.OrderDetailMapper;
import com.sky.mapper.OrderMapper;
import com.sky.mapper.ShoppingCartMapper;
import com.sky.result.PageResult;
import com.sky.service.OrderService;
import com.sky.vo.OrderPaymentVO;
import com.sky.vo.OrderStatisticsVO;
import com.sky.vo.OrderSubmitVO;
import com.sky.vo.OrderVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderMapper orderMapper;

    @Autowired
    private OrderDetailMapper orderDetailMapper;

    @Autowired
    private ShoppingCartMapper shoppingCartMapper;

    @Autowired
    private AddressBookMapper addressBookMapper;

    //下单
    @Override
    @Transactional
    public OrderSubmitVO submitOrder(OrdersSubmitDTO ordersSubmitDTO) {
        // 获取当前用户id
        Long userId = BaseContext.getCurrentId();

        // 查询购物车，如果为空就不让下单
        List<ShoppingCart> shoppingCartList = shoppingCartMapper.listByUserId(userId);
        if (shoppingCartList == null || shoppingCartList.isEmpty()) {
            throw new RuntimeException("购物车为空，无法下单");
        }

        // 查询地址如果查不到就不让下单
        AddressBook addressBook = addressBookMapper.getById(ordersSubmitDTO.getAddressBookId());
        if (addressBook == null) {
            throw new RuntimeException("地址信息不存在，请重新选择");
        }

        // 计算订单总金额
        BigDecimal totalAmount = BigDecimal.ZERO;
        for (ShoppingCart cart : shoppingCartList) {
            BigDecimal itemTotal = cart.getAmount().multiply(new BigDecimal(cart.getNumber()));
            // 累加到总金额
            totalAmount = totalAmount.add(itemTotal);
        }

        // 构建订单对象
        Orders orders = new Orders();
        // 把 DTO 中的字段拷贝到 orders 中
        BeanUtils.copyProperties(ordersSubmitDTO, orders);
        orders.setUserId(userId);
        orders.setNumber(generateOrderNumber(userId));
        orders.setStatus(1);
        orders.setPayStatus(0);
        orders.setOrderTime(LocalDateTime.now());
        orders.setAmount(totalAmount);

        // 从地址簿补全收货信息
        orders.setPhone(addressBook.getPhone());
        orders.setConsignee(addressBook.getConsignee());
        orders.setUserName(addressBook.getConsignee());

        // 拼接完整地址：省 + 市 + 区 + 详细地址
        String province = addressBook.getProvinceName() == null ? "" : addressBook.getProvinceName();
        String city     = addressBook.getCityName()     == null ? "" : addressBook.getCityName();
        String district = addressBook.getDistrictName() == null ? "" : addressBook.getDistrictName();
        String detail   = addressBook.getDetail()       == null ? "" : addressBook.getDetail();
        orders.setAddress(province + city + district + detail);

        // 插入订单主表
        orderMapper.insert(orders);

        // 把购物车里的每一条都转成订单明细，然后批量插入
        List<OrderDetail> orderDetailList = new ArrayList<>();
        for (ShoppingCart cart : shoppingCartList) {
            OrderDetail orderdetail = new OrderDetail();
            BeanUtils.copyProperties(cart, orderdetail);
            orderdetail.setId(null);
            orderdetail.setOrderId(orders.getId());
            orderDetailList.add(orderdetail);
        }
        orderDetailMapper.insertBatch(orderDetailList);

        // 下单成功后清空购物车
        shoppingCartMapper.deleteByUserId(userId);

        // 组装返回结果
        OrderSubmitVO vo = new OrderSubmitVO();
        vo.setId(orders.getId());
        vo.setOrderNumber(orders.getNumber());
        vo.setOrderAmount(orders.getAmount());
        vo.setOrderTime(orders.getOrderTime());
        return vo;
    }

    /**
     * 生成订单号：当前时间（年月日时分秒）+ 用户id
     */
    private String generateOrderNumber(Long userId) {
        String time = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
        return time + userId;
    }
    // 订单分页查询
    @Override
    public PageResult conditionSearch(OrdersPageQueryDTO dto) {
        PageHelper.startPage(dto.getPage(), dto.getPageSize());
        // 查询订单列表
        Page<Orders> page = orderMapper.pageQuery(dto);
        // 把每条订单转成 OrderVO，并补充明细和 orderDishes 字段
        List<OrderVO> voList = new ArrayList<>();
        for (Orders orders : page.getResult()) {
            OrderVO vo = new OrderVO();
            BeanUtils.copyProperties(orders, vo);
            // 查询该订单的明细
            List<OrderDetail> detailList = orderDetailMapper.getByOrderId(orders.getId());
            vo.setOrderDetailList(detailList);
            vo.setOrderDishes(buildOrderDishes(detailList));
            voList.add(vo);
        }
        // 返回总记录数和当前页数据
        return new PageResult(page.getTotal(), voList);
    }

    // 各状态订单数量统计
    @Override
    public OrderStatisticsVO statistics() {
        OrderStatisticsVO vo = new OrderStatisticsVO();
        vo.setToBeConfirmed(orderMapper.countStatus(2));
        vo.setConfirmed(orderMapper.countStatus(3));
        vo.setDeliveryInProgress(orderMapper.countStatus(4));
        return vo;
    }

    //订单详情
    @Override
    public OrderVO details(Long id) {
        // 先查询订单
        Orders orders = orderMapper.getById(id);
        if (orders == null) {
            throw new RuntimeException("订单不存在");
        }

        // 把 orders 的字段拷贝到 vo
        OrderVO vo = new OrderVO();
        BeanUtils.copyProperties(orders, vo);

        // 再查询订单明细
        List<OrderDetail> detailList = orderDetailMapper.getByOrderId(id);
        vo.setOrderDetailList(detailList);
        vo.setOrderDishes(buildOrderDishes(detailList));

        return vo;
    }

    // 接单
    @Override
    public void confirm(OrdersConfirmDTO dto) {
        Orders orders = new Orders();
        orders.setId(dto.getId());
        orders.setStatus(3);
        orderMapper.update(orders);
    }

    // 拒单
    @Override
    @Transactional
    public void rejection(OrdersRejectionDTO dto) {
        // 先查询订单，只有状态为待接单才能拒单
        Orders dbOrder = orderMapper.getById(dto.getId());
        if (dbOrder == null || dbOrder.getStatus() != 2) {
            throw new RuntimeException("订单状态不正确，无法拒单");
        }

        Orders orders = new Orders();
        orders.setId(dto.getId());
        orders.setStatus(6);
        orders.setRejectionReason(dto.getRejectionReason());
        orders.setCancelTime(LocalDateTime.now());
        orderMapper.update(orders);
    }

    // 取消订单
    @Override
    @Transactional
    public void cancel(OrdersCancelDTO dto) {
        Orders orders = new Orders();
        orders.setId(dto.getId());
        orders.setStatus(6);
        orders.setCancelReason(dto.getCancelReason());
        orders.setCancelTime(LocalDateTime.now());
        orderMapper.update(orders);
    }
    //派送订单
    @Override
    public void delivery(Long id) {
        Orders orders = new Orders();
        orders.setId(id);
        orders.setStatus(4);   // 4 派送中
        orderMapper.update(orders);
    }
    // 完成订单
    @Override
    public void complete(Long id) {
        Orders orders = new Orders();
        orders.setId(id);
        orders.setStatus(5);   // 5 已完成
        orders.setDeliveryTime(LocalDateTime.now());
        orderMapper.update(orders);
    }
    // 工具方法把订单明细拼成菜名*数量 菜名*数量
    private String buildOrderDishes(List<OrderDetail> detailList) {
        StringBuilder sb = new StringBuilder();
        if (detailList != null) {
            for (OrderDetail detail : detailList) {
                sb.append(detail.getName());
                sb.append("*");
                sb.append(detail.getNumber());
                sb.append("；");
            }
        }
        return sb.toString();
    }
    /**
     * 微信支付
     */
    public OrderPaymentVO payment(OrdersPaymentDTO ordersPaymentDTO) {
        /*
         * 假设这里是微信支付的代码（暂时不用实现）
         */
        //支付成功 更新订单状态
        //更新订单状态为待接单
        //支付状态为已支付
        //根据订单号查询订单
        String orderNumber = ordersPaymentDTO.getOrderNumber();
        Orders orders = orderMapper.getByNumber(orderNumber);
        //判断是否存在该订单
        if (orders == null) {
            throw new RuntimeException("订单不存在");
        }
        //更新订单数据
        Orders updateOrder = new Orders();
        updateOrder.setId(orders.getId());
        updateOrder.setStatus(2);
        updateOrder.setPayStatus(1);
        updateOrder.setCheckoutTime(LocalDateTime.now());
        updateOrder.setPayMethod(ordersPaymentDTO.getPayMethod());
        orderMapper.update(updateOrder);
        OrderPaymentVO vo = new OrderPaymentVO();
        vo.setNonceStr("itcast");
        vo.setPaySign("itcast");
        vo.setTimeStamp(String.valueOf(System.currentTimeMillis() / 1000));
        vo.setSignType("RSA");
        vo.setPackageStr("prepay_id=itcast");
        return vo;
    }
}