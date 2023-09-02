package com.Ray.springbootmall.service;

import com.Ray.springbootmall.dto.CreateOrderRequest;
import com.Ray.springbootmall.dto.OrderQueryParams;
import com.Ray.springbootmall.model.Order;

import java.util.List;

public interface OrderService {
    // 計算訂單數量
    Integer countOrder(OrderQueryParams orderQueryParams);
    // 查詢訂單
    List<Order> getOrders(OrderQueryParams orderQueryParams);
    // 創建訂單
    Integer createOrder(Integer userId, CreateOrderRequest createOrderRequest);
    // 根據訂單ID查詢訂單資訊
    Order getOrderById(Integer orderId);
}
