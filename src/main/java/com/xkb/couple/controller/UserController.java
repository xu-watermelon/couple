package com.xkb.couple.controller;

import com.xkb.couple.core.common.resp.BaseResponse;
import com.xkb.couple.core.utils.LogDesensitizeUtil;
import com.xkb.couple.pojo.dto.LoginDTO;
import com.xkb.couple.pojo.dto.RegisterDTO;
import com.xkb.couple.pojo.vo.LoginResponseVO;
import com.xkb.couple.pojo.vo.UserVO;
import com.xkb.couple.service.UserService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
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
        log.info("用户登录：email={}, password={}", LogDesensitizeUtil.desensitizeEmail(loginDTO.getEmail()), LogDesensitizeUtil.desensitizePassword(loginDTO.getPassword()));
        return userService.login(loginDTO);
    }
    /**
     * 注册
     * @param registerDTO 注册参数 DTO
     * @return BaseResponse<User>
     */
    @PostMapping("/register")
    public BaseResponse<LoginResponseVO> register(@RequestBody @Valid RegisterDTO registerDTO) {
        log.info("用户注册：email={}, password={}", LogDesensitizeUtil.desensitizeEmail(registerDTO.getEmail()), LogDesensitizeUtil.desensitizePassword(registerDTO.getPassword()));
        return userService.register(registerDTO);
    }
    /**
     * 获取当前用户信息
     * @param id 用户id
     * @return BaseResponse<UserVO>
     */
    @GetMapping("/info/{id}")
    public BaseResponse<UserVO> getUserInfo(@PathVariable Long id) {
        return userService.getUserInfo(id);
    }

    /**
     * 获取验证码
     * @param email 邮箱
     * @param type 验证码类型：register（注册）、forget（忘记密码）、login（登录）
     * @return BaseResponse<String> 验证码id
     */
    @GetMapping("/captcha")
    public BaseResponse<String> getCaptcha(@RequestParam @Email String email,
                                           @RequestParam @NotNull String type) {
        return userService.getCaptcha(email, type);
    }




}
