package com.Ray.springbootmall.controller;

import com.Ray.springbootmall.dto.OrderQueryParams;
import com.Ray.springbootmall.service.OrderService;
import com.Ray.springbootmall.dto.CreateOrderRequest;
import com.Ray.springbootmall.model.Order;
import com.Ray.springbootmall.util.Page;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class OrderController {

    @Autowired
    private OrderService orderService;

    @GetMapping("/users/{userId}/orders")
    public ResponseEntity<Page<Order>> getOrder(
//          從 URL 中讀取 userId
            @PathVariable Integer userId,
//          從 URL 中讀取 limit 和 offset 參數
            @RequestParam(defaultValue = "10") @Max(1000) @Min(0) Integer limit,
            @RequestParam(defaultValue = "0") @Min(0) Integer offset
    ){
        OrderQueryParams orderQueryParams = new OrderQueryParams();
        orderQueryParams.setUserId(userId);
        orderQueryParams.setLimit(limit);
        orderQueryParams.setOffset(offset);

//          取得 order list
        List<Order> orderList = orderService.getOrders(orderQueryParams);

//          取得 order 總數
        Integer count = orderService.countOrder(orderQueryParams);

//          分頁
        Page<Order> page = new Page<>();
        page.setLimit(limit);
        page.setOffset(offset);
        page.setTotal(count);
        page.setResults(orderList);

        return ResponseEntity.ok().body(page);

    }


//          ("電商網站眾多帳號中/userId 的帳號底下/創建一筆訂單出來")
    @PostMapping("/users/{userId}/orders")
    public ResponseEntity<?> createOrder(@PathVariable Integer userId,
                                         @RequestBody @Valid CreateOrderRequest createOrderRequest){
//          創建訂單
        Integer orderId = orderService.createOrder(userId, createOrderRequest);
//          獲取創建的訂單資訊
        Order order = orderService.getOrderById(orderId);

        return ResponseEntity.status(HttpStatus.CREATED).body(order);
    }
}
