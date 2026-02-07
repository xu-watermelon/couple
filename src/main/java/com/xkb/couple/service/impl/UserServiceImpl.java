package com.xkb.couple.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.xkb.couple.core.common.resp.BaseResponse;
import com.xkb.couple.mapper.UserMapper;
import com.xkb.couple.pojo.entity.User;
import com.xkb.couple.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

/**
 * 用户服务实现类
 * @author xuwatermelon
 * @date 2026/02/07
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserMapper userMapper;

    @Override
    public BaseResponse<User> login(String username, String password) {
        // 1. 校验参数
        if (username == null || password == null) {
            return BaseResponse.fail("用户名或密码不能为空");
        }
        // 2. 查询用户
        User user = userMapper.selectOne(new LambdaQueryWrapper<User>()
                .eq(User::getUsername, username));
        if (user == null) {
            return BaseResponse.fail("用户不存在");
        }
        // 3. 校验密码(md5)
        // 密码md5加密
        password = DigestUtils.md5DigestAsHex(password.getBytes());
        if (!user.getPassword().equals(password)) {
            return BaseResponse.fail("密码错误");

        }
        return BaseResponse.success(user);
    }
}
