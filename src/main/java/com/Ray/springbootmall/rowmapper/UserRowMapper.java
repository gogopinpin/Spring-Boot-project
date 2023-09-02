package com.Ray.springbootmall.rowmapper;

import com.Ray.springbootmall.model.User;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class UserRowMapper implements RowMapper<User> {

    @Override
//      創建一個新的 User 物件
    public User mapRow(ResultSet rs, int rowNum) throws SQLException{
        User user = new User();

//      從 ResultSet 中讀取相關的欄位並將其設定到 User 物件中
        user.setUserId(rs.getInt("user_id"));
        user.setEmail(rs.getString("email"));
        user.setPassword(rs.getString("password"));
        user.setCreatedDate(rs.getTimestamp("created_date"));
        user.setLastModifiedDate(rs.getTimestamp("last_modified_date"));

//      返回映射後的 User 物件
        return user;

    }
}
