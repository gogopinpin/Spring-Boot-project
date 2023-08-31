package com.Ray.springbootmall.dao;

import com.Ray.springbootmall.dto.OrderQueryParams;
import com.Ray.springbootmall.model.Order;
import com.Ray.springbootmall.model.OrderItem;

import java.util.List;

public interface OrderDao {
//    返回符合特定查詢條件的訂單總數量
    Integer countOrder(OrderQueryParams orderQueryParams);
//    返回符合特定查詢條件的訂單列表
    List<Order> getOrders(OrderQueryParams orderQueryParams);
//    根據訂單 ID 返回該訂單的詳細資訊
    Order getOrderById(Integer orderId);
//    根據訂單 ID 返回該訂單的項目列表，每個項目對應訂單中的一個商品。
    List<OrderItem> getOrderItemsById(Integer orderId);
//    創建一筆新的訂單，並返回創建的訂單 ID
    Integer createOrder(Integer userId, Integer totalAmount);
//    創建訂單項目列表
    void createOrderItems(Integer orderId, List<OrderItem> orderItemList);
}


















