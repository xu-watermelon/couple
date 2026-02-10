package com.xkb.couple.controller;

import com.xkb.couple.core.common.resp.BaseResponse;
import com.xkb.couple.core.utils.LogDesensitizeUtil;
import com.xkb.couple.pojo.dto.LoginDTO;
import com.xkb.couple.pojo.dto.RegisterDTO;
import com.xkb.couple.pojo.vo.LoginResponseVO;
import com.xkb.couple.pojo.vo.UserVO;
import com.xkb.couple.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

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
     * @param loginDTO 登录参数 DTO
     * @return BaseResponse<User>
     */
    @PostMapping("/login")
    public BaseResponse<LoginResponseVO> login(@RequestBody @Valid LoginDTO loginDTO) {
        log.info("用户登录：username={}, password={}", LogDesensitizeUtil.desensitizeUsername(loginDTO.getUsername()), LogDesensitizeUtil.desensitizePassword(loginDTO.getPassword()));
        return userService.login(loginDTO);
    }
    /**
     * 注册
     * @param registerDTO 注册参数 DTO
     * @return BaseResponse<User>
     */
    @PostMapping("/register")
    public BaseResponse<UserVO> register(@RequestBody @Valid RegisterDTO registerDTO) {
        log.info("用户注册：username={}, password={}", LogDesensitizeUtil.desensitizeUsername(registerDTO.getUsername()), LogDesensitizeUtil.desensitizePassword(registerDTO.getPassword()));
        return userService.register(registerDTO);
    }
    /**
     * 获取用户信息
     * @param id 用户id
     * @return BaseResponse<UserVO>
     */
    @GetMapping("/info/{id}")
    public BaseResponse<UserVO> getUserInfo(@PathVariable Long id) {
        return userService.getUserInfo(id);
    }
}
