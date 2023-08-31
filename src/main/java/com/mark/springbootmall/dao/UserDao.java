package com.Ray.springbootmall.dao;

import com.Ray.springbootmall.dto.UserRegisterRequest;
import com.Ray.springbootmall.model.User;

public interface UserDao {
//    創建一個新的使用者，並返回創建的使用者 ID
    Integer createUser(UserRegisterRequest userRegisterRequest);
//    根據使用者的電子郵件地址返回該使用者的詳細資訊。
    User getUserByEmail(String email);
//    根據使用者的 ID 返回該使用者的詳細資訊
    User getUserById(Integer userId);
}
