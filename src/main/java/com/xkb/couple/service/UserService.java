package com.xkb.couple.service;

import com.xkb.couple.core.common.resp.BaseResponse;
import com.xkb.couple.pojo.dto.*;
import com.xkb.couple.pojo.vo.LoginResponseVO;
import com.xkb.couple.pojo.vo.UserVO;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;

/**
 * 用户服务接口
 *
 *  @author xuwatermelon
 *  @date 2026/02/07
 **/

public interface UserService {


    /**
     * 密码登录
     * @param passwordLoginDTO 登录参数 DTO
     * @return BaseResponse<LoginResponseVO>
     */
    BaseResponse<LoginResponseVO> login(PasswordLoginDTO passwordLoginDTO);
    /**
     * 验证码登录
     * @param captchaLoginDTO 登录参数 DTO
     * @return BaseResponse<LoginResponseVO>
     */
    BaseResponse<LoginResponseVO> loginByCaptcha(@Valid CaptchaLoginDTO captchaLoginDTO);
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
     * 获取验证码
     * @param email 邮箱
     * @param type 验证码类型
     * @return BaseResponse<String> 验证码
     */
    BaseResponse<String> getCaptcha(@Email String email, @NotNull String type);

    /**
     * 忘记密码
     * @param forgetPasswordDTO 忘记密码参数 DTO
     * @return BaseResponse<Void> 无返回值
     **/
    BaseResponse<Void> forgetPassword(ForgetPasswordDTO forgetPasswordDTO);
    /**
     * 修改密码
     * @param updatePasswordDTO 修改密码参数 DTO
     * @return BaseResponse<Void> 无返回值
     **/
    BaseResponse<Void> updatePassword(@Valid UpdatePasswordDTO updatePasswordDTO);
}
