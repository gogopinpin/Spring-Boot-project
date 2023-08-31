package com.Ray.springbootmall.controller;

import com.Ray.springbootmall.dto.UserLoginRequest;
import com.Ray.springbootmall.service.UserService;
import com.Ray.springbootmall.dto.UserRegisterRequest;
import com.Ray.springbootmall.model.User;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {

    @Autowired
    private UserService userService;

//      註冊新使用者
    @PostMapping("/users/register")
//      從請求的主體中讀取 UserRegisterRequest 物件，並通過 @Valid 執行對物件的驗證
    public ResponseEntity<User> register(@RequestBody @Valid UserRegisterRequest userRegisterRequest) {
        Integer userId = userService.register(userRegisterRequest);

        User user = userService.getUserById(userId);

        return ResponseEntity.status(HttpStatus.CREATED).body(user);
    }
//      使用者登入
    @PostMapping("/users/login")
//      讀取請求的主體並驗證內容
    public  ResponseEntity<User> login(@RequestBody @Valid UserLoginRequest userLoginRequest){

        User user = userService.login(userLoginRequest);

        return ResponseEntity.ok().body(user);
    }
}
























