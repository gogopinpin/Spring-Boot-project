package com.Ray.springbootmall.service.impl;


import com.Ray.springbootmall.dto.UserLoginRequest;
import com.Ray.springbootmall.dto.UserRegisterRequest;
import com.Ray.springbootmall.service.UserService;
import com.Ray.springbootmall.dao.UserDao;
import com.Ray.springbootmall.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.util.DigestUtils;
import org.springframework.web.server.ResponseStatusException;

@Component
public class UserServiceImpl implements UserService {

    private final static Logger log = LoggerFactory.getLogger(UserServiceImpl.class);

    @Autowired
    private UserDao userDao;

    @Override
//      根據提供的用戶ID（userId）查詢用戶資訊。

    public User getUserById(Integer userId) {
        return userDao.getUserById(userId);
    }

    @Override
//      註冊新用戶

    public Integer register(UserRegisterRequest userRegisterRequest) {
//      檢查註冊的 emaill
        User user = userDao.getUserByEmail(userRegisterRequest.getEmail());

        if (user != null) {
            log.warn("該 emaill {} 已被註冊", userRegisterRequest.getEmail());
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
//      使用 MD5 生成密碼的雜湊值
        String hashedPassword = DigestUtils.md5DigestAsHex(userRegisterRequest.getPassword().getBytes());
        userRegisterRequest.setPassword(hashedPassword);

//      創建帳號
        return userDao.createUser(userRegisterRequest);
    }

    @Override
//      用戶登入

    public User login(UserLoginRequest userLoginRequest) {
        User user = userDao.getUserByEmail(userLoginRequest.getEmail());

//      檢查 User 是否存在
        if (user == null) {
            log.warn("該 emaill {} 尚未註冊", userLoginRequest.getEmail());
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }

//      使用 MD5 生成密碼的雜湊值
        String hashedPassword = DigestUtils.md5DigestAsHex(userLoginRequest.getPassword().getBytes());

//      檢查密碼是否與資料庫相同
        if (user.getPassword().equals(hashedPassword)) {
            return user;
        } else {
            log.warn("該 emaill {} 密碼不正確", userLoginRequest.getEmail());
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
    }
}
