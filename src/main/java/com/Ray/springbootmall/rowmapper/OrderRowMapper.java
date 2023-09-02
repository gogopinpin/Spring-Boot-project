package com.Ray.springbootmall.rowmapper;

import com.Ray.springbootmall.model.Order;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class OrderRowMapper implements RowMapper<Order> {

    @Override
    public Order mapRow(ResultSet rs, int rowNum) throws SQLException {
//      創建一個新的 Order 物件
        Order order = new Order();

//      從 ResultSet 中讀取相關的欄位並將其設定到 Order 物件中
        order.setOrderId(rs.getInt("order_id"));
        order.setUserId(rs.getInt("user_id"));
        order.setTotalAmount(rs.getInt("total_amount"));
        order.setCreatedDate(rs.getTimestamp("created_date"));
        order.setLastModifiedDate(rs.getTimestamp("last_modified_date"));

//      返回映射後的 Order 物件
        return order;
    }
}














