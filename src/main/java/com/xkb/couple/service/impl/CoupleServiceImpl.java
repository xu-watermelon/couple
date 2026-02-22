package com.xkb.couple.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.xkb.couple.core.common.constants.ErrorCodeEnum;
import com.xkb.couple.core.common.constants.SystemConfigConstans;
import com.xkb.couple.core.common.resp.BaseResponse;
import com.xkb.couple.core.constants.UserConstans;
import com.xkb.couple.core.utils.UserHolder;
import com.xkb.couple.mapper.CoupleMapper;
import com.xkb.couple.mapper.UserMapper;
import com.xkb.couple.pojo.dto.NotificationDTO;
import com.xkb.couple.pojo.entity.Couple;
import com.xkb.couple.pojo.entity.User;
import com.xkb.couple.service.CoupleService;
import com.xkb.couple.service.WebSocketService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 情侣服务实现类
 * @author xuwatermelon
 * @date 2026/02/18
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class CoupleServiceImpl implements CoupleService {
    private final CoupleMapper coupleMapper;
    private final UserMapper userMapper;
    private final WebSocketService webSocketService;
    /**
     * @param email 对方邮箱
     */
    @Override
    public BaseResponse<Void> bindCouple(String email) {
        //1. 验证邮箱格式
        if (!email.matches(SystemConfigConstans.EMAIL_REGEX)) {
            log.warn("绑定情侣，邮箱格式错误：{}", email);
            return BaseResponse.fail(ErrorCodeEnum.EMAIL_FORMAT_ERROR);
        }
        //2. 验证用户是否存在
        User user = userMapper.selectOne(new LambdaQueryWrapper<User>().eq(User::getEmail, email));
        if (user == null) {
            log.warn("绑定情侣，用户不存在：{}", email);
            return BaseResponse.fail(ErrorCodeEnum.USER_NOT_EXIST);
        }
        //3. 验证用户是否已绑定情侣
        if (user.getHasCouple() == 1) {
            log.warn("绑定情侣，用户已绑定情侣：{}", email);
            return BaseResponse.fail(ErrorCodeEnum.USER_HAS_COUPLE);
        }
        //4. 验证用户性别是否匹配
        User currentUser = userMapper.selectOne(new LambdaQueryWrapper<User>().eq(User::getId, UserHolder.getUserId()));
        if (currentUser == null) {
            log.warn("绑定情侣，用户不存在：{}", UserHolder.getUserId());
            return BaseResponse.fail(ErrorCodeEnum.USER_NOT_EXIST);
        }
        if (user.getGender().equals(currentUser.getGender())) {
            log.warn("绑定情侣，用户性别错误：{}", email);
            return BaseResponse.fail(ErrorCodeEnum.USER_GENDER_ERROR);
        }
        Couple couple;
        if (currentUser.getGender().equals(UserConstans.UserEnum.FEMALE.getValue())) {
            couple = Couple.builder()
                    .maleUserId(user.getId())
                    .femaleUserId(currentUser.getId())
                    .createTime(LocalDateTime.now())
                    .updateTime(LocalDateTime.now())
                    .build();

        } else {
            couple = Couple.builder()
                    .maleUserId(currentUser.getId())
                    .femaleUserId(user.getId())
                    .createTime(LocalDateTime.now())
                    .updateTime(LocalDateTime.now())
                    .build();
        }

        coupleMapper.insert(couple);
        //4. 发送绑定情侣请求通知TODO 抽出来常数
       webSocketService.sendNotification(
                NotificationDTO.builder()
                        .senderId(currentUser.getId())
                        .receiverId(user.getId())
                        .type("couple_request")
                        .title("绑定情侣请求")
                        .content("您有一个绑定情侣的请求，请确认是否同意。"+currentUser.getNickname())
                        .relatedId(couple.getId())
                        .timestamp(LocalDateTime.now())
                        .status("0")
                        .createTime(LocalDateTime.now())
                        .build()
        );
            return BaseResponse.success();
    }
}
