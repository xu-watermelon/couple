package com.xkb.couple.service;

import com.xkb.couple.core.common.resp.BaseResponse;
import com.xkb.couple.pojo.entity.User;

/**
 * 用户服务接口
 *
 *  @author xuwatermelon
 *  @date 2026/02/07
 **/

public interface UserService {


    /**
     * 登录
     * @param username 用户名
     * @param password 密码
     * @return BaseResponse
     */
    BaseResponse<User> login(String username, String password);
}
