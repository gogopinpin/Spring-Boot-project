package com.Ray.springbootmall.service.impl;

import com.Ray.springbootmall.dto.BuyItem;
import com.Ray.springbootmall.dto.CreateOrderRequest;
import com.Ray.springbootmall.dto.OrderQueryParams;
import com.Ray.springbootmall.model.Product;
import com.Ray.springbootmall.service.OrderService;
import com.Ray.springbootmall.dao.OrderDao;
import com.Ray.springbootmall.dao.ProductDao;
import com.Ray.springbootmall.dao.UserDao;
import com.Ray.springbootmall.model.Order;
import com.Ray.springbootmall.model.OrderItem;
import com.Ray.springbootmall.model.User;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;

@Component
public class OrderServiceImpl implements OrderService {

    private final static Logger log = LoggerFactory.getLogger(OrderServiceImpl.class);

    @Autowired
    private OrderDao orderDao;

    @Autowired
    private ProductDao productDao;

    @Autowired
    private UserDao userDao;

//    實現 OrderService 介面的方法
    @Override
//    根據提供的訂單查詢條件，返回符合條件的訂單總數量。
    public Integer countOrder(OrderQueryParams orderQueryParams) {
        return orderDao.countOrder(orderQueryParams);
    }

    @Override
//    根據提供的訂單查詢條件，返回符合條件的訂單列表。
    public List<Order> getOrders(OrderQueryParams orderQueryParams) {
        List<Order> orderList = orderDao.getOrders(orderQueryParams);

        for (Order order : orderList) {
            List<OrderItem> orderItemList = orderDao.getOrderItemsById(order.getOrderId());

            order.setOrderItemList(orderItemList);
        }
        return orderList;
    }

    @Override
//      根據提供的訂單ID（orderId），返回該訂單的詳細資訊，包括訂單項目。

    public Order getOrderById(Integer orderId) {
        Order order = orderDao.getOrderById(orderId);

        List<OrderItem> orderItemList = orderDao.getOrderItemsById(orderId);

        order.setOrderItemList(orderItemList);

        return order;
    }

    @Transactional
//      修改多張資料庫 table 確保一起發生(All or Never)
    @Override
//      創建訂單的核心方法。
    public Integer createOrder(Integer userId, CreateOrderRequest createOrderRequest) {

//      檢查userId 是否存在
        User user = userDao.getUserById(userId);

        if (user == null) {
            log.warn("該 userId{} 不存在", userId);
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }

        int totalAmount = 0;
        List<OrderItem> orderItemList = new ArrayList<>();

        for (BuyItem buyItem : createOrderRequest.getBuyItemList()) {
            Product product = productDao.getProductById(buyItem.getProductId());

//      檢查 product 是否存在、庫存是否足夠
            if (product == null) {
                log.warn("商品 {} 不存在", buyItem.getProductId());
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST);

            } else if (product.getStock() < buyItem.getQuantity()) {
                log.warn("商品 {} 庫存不足。 剩餘庫存 {} 、欲購買數量 {}", buyItem.getProductId(), product.getStock(), buyItem.getQuantity());
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
            }

//      購買後扣除商品庫存
            productDao.updateStock(product.getProductId(), product.getStock() - buyItem.getQuantity());

//      計算總價
            int amount = buyItem.getQuantity() * product.getPrice();
            totalAmount = totalAmount + amount;

//      轉換 BuyItem to OrderItem
            OrderItem orderItem = new OrderItem();
            orderItem.setProductId(buyItem.getProductId());
            orderItem.setQuantity(buyItem.getQuantity());
            orderItem.setAmount(amount);

            orderItemList.add(orderItem);

        }
//      創建訂單
        Integer orderId = orderDao.createOrder(userId, totalAmount);

        orderDao.createOrderItems(orderId, orderItemList);

        return orderId;
    }
}
//      通過調用 orderDao、productDao 和 userDao 的方法來與資料庫進行交互，執行訂單相關的操作。
