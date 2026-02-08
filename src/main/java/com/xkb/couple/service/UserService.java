package com.xkb.couple.service;

import com.xkb.couple.core.common.resp.BaseResponse;
import com.xkb.couple.pojo.dto.LoginDTO;
import com.xkb.couple.pojo.dto.RegisterDTO;
import com.xkb.couple.pojo.entity.User;
import com.xkb.couple.pojo.vo.LoginResponseVO;

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
     * @param registerDTO 注册参数 DTO
     * @return BaseResponse<User>
     */
    BaseResponse<User> register(RegisterDTO registerDTO);
}
