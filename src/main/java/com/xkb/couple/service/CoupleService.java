package com.xkb.couple.service;


import com.xkb.couple.core.common.resp.BaseResponse;
import jakarta.validation.constraints.Email;
/**
 * 情侣服务
 * @author xuwatermelon
 * @date 2026/02/18
 */

public interface CoupleService  {
    /**
     * 申请绑定
     * @param email 对方邮箱
     */
     BaseResponse<Void> bindCouple(@Email String email);
}
