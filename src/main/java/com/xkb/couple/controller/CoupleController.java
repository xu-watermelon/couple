package com.xkb.couple.controller;

import com.xkb.couple.core.common.resp.BaseResponse;
import com.xkb.couple.service.CoupleService;
import jakarta.validation.constraints.Email;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 情侣控制器
 * @author xuwatermelon
 * @date 2026/02/18
 */
@RestController
@RequestMapping("/couple")
@RequiredArgsConstructor
@Slf4j
public class CoupleController {

    private final CoupleService coupleService;

    /**
     * 申请绑定
     *
     * @param email 对方邮箱
     */
    @PostMapping("/bind/{email}")
    public BaseResponse<Void> bindCouple(@PathVariable @Email String email) {
        log.info("申请绑定情侣，对方邮箱：{}", email);
        return coupleService.bindCouple(email);

    }

    /**
     * 同意绑定
     */
    @PostMapping("/agreeBind")
    public BaseResponse<Void> agreeBindCouple() {
        return BaseResponse.success();
    }

}
