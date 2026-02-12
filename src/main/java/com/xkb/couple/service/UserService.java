package com.xkb.couple.service;

import com.xkb.couple.core.common.resp.BaseResponse;
import com.xkb.couple.pojo.dto.LoginDTO;
import com.xkb.couple.pojo.dto.RegisterDTO;
import com.xkb.couple.pojo.vo.LoginResponseVO;
import com.xkb.couple.pojo.vo.UserVO;
import jakarta.validation.constraints.Email;

/**
 * 用户服务接口
 *
 *  @author xuwatermelon
 *  @date 2026/02/07
 **/

public interface UserService {


    /**
     * 登录
     * @param loginDTO 登录参数 DTO
     * @return BaseResponse<LoginResponseVO>
     */
    BaseResponse<LoginResponseVO> login(LoginDTO loginDTO);
    /**
     * 注册
     *
     * @param registerDTO 注册参数 DTO
     * @return BaseResponse<User>
     */
    BaseResponse<LoginResponseVO> register(RegisterDTO registerDTO);
    /**
     * 获取用户信息
     * @param id 用户id
     * @return BaseResponse<UserVO>
     */
    BaseResponse<UserVO> getUserInfo(Long id);

    /**
     * 获取注册验证码
     * @param email 邮箱
     * @return BaseResponse<String> 验证码
     */
    BaseResponse<String> getRegisterCaptcha(@Email String email);
     /**
     * 获取忘记密码验证码
     * @param email 邮箱
     * @return BaseResponse<String> 验证码
     */
    BaseResponse<String> getForgetCaptcha(@Email String email);
}
