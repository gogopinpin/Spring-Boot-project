package com.Ray.springbootmall.service;

import com.Ray.springbootmall.dto.UserLoginRequest;
import com.Ray.springbootmall.dto.UserRegisterRequest;
import com.Ray.springbootmall.model.User;

public interface UserService {
    // 使用者註冊，傳入註冊請求的資訊，返回使用者的唯一識別號
    Integer register(UserRegisterRequest userRegisterRequest);
    // 根據使用者唯一識別號（userId）查詢並返回相對應的使用者資訊
    User getUserById(Integer userId);
    // 使用者登入，傳入登入請求的資訊，返回使用者的資訊（User 物件）
    User login(UserLoginRequest userLoginRequest);

}
