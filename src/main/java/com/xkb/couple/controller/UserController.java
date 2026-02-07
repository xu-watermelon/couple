package com.xkb.couple.controller;

import com.xkb.couple.core.common.resp.BaseResponse;
import com.xkb.couple.pojo.entity.User;
import com.xkb.couple.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.annotations.Update;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 用户控制器
 * @author xuwatermelon
 * @date 2026/02/07
 */
@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/api/user")
public class UserController {
    private final UserService userService;
    /**
     * 登录
     * @param username 用户名
     * @param password 密码
     * @return BaseResponse<User>
     */
    @PostMapping("/login")
    public BaseResponse<User> login(String username, String password) {
        log.info("用户登录：username={}, password={}", username, password);
        return userService.login(username, password);
    }
}
