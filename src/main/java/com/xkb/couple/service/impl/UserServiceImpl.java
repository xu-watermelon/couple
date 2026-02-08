package com.xkb.couple.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.xkb.couple.core.common.resp.BaseResponse;
import com.xkb.couple.core.utils.JwtUtil;
import com.xkb.couple.core.utils.LogDesensitizeUtil;
import com.xkb.couple.mapper.UserMapper;
import com.xkb.couple.pojo.dto.LoginDTO;
import com.xkb.couple.pojo.dto.RegisterDTO;
import com.xkb.couple.pojo.entity.User;
import com.xkb.couple.pojo.vo.LoginResponseVO;
import com.xkb.couple.pojo.vo.UserVO;
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
    private final JwtUtil jwtUtil;

    @Override
    public BaseResponse<LoginResponseVO> login(LoginDTO loginDTO) {
        // 1. 校验参数
        if (loginDTO.getUsername() == null || loginDTO.getPassword() == null) {
            log.info("用户登录失败(用户名或密码为空)：username={}, password={}", LogDesensitizeUtil.desensitizeUsername(loginDTO.getUsername()), LogDesensitizeUtil.desensitizePassword(loginDTO.getPassword()));
            return BaseResponse.fail("用户名或密码不能为空");
        }
        // 2. 查询用户
        User user = userMapper.selectOne(new LambdaQueryWrapper<User>()
                .eq(User::getUsername, loginDTO.getUsername()));
        if (user == null) {
            log.info("用户登录失败(用户不存在)：username={}", LogDesensitizeUtil.desensitizeUsername(loginDTO.getUsername()));
            return BaseResponse.fail("用户不存在");
        }
        // 3. 校验密码(md5)
        // 密码md5加密
        String password = DigestUtils.md5DigestAsHex(loginDTO.getPassword().getBytes());
        if (!user.getPassword().equals(password)) {
            log.info("用户登录失败(密码错误)：username={}, password={}", LogDesensitizeUtil.desensitizeUsername(loginDTO.getUsername()), LogDesensitizeUtil.desensitizePassword(loginDTO.getPassword()));
            return BaseResponse.fail("密码错误");
        }
        // 4. 生成token
        String token = jwtUtil.generateToken(user.getId());
        UserVO userVO = UserVO.builder()
                .id(user.getId())
                .nickname(user.getNickname())
                .username(user.getUsername())
                .hasCouple(user.getHasCouple())
                .avatar(user.getAvatar())
                .gender(user.getGender())
                .birthday(user.getBirthday())
                .build();

        log.info("用户登录成功：username={}, token={}", LogDesensitizeUtil.desensitizeUsername(loginDTO.getUsername()), LogDesensitizeUtil.desensitizeToken(token));
        LoginResponseVO loginResponseVO = LoginResponseVO.builder()
                .token(token)
                .userVO(userVO)
                .build();
        return BaseResponse.success(loginResponseVO);
    }

    @Override
    public BaseResponse<User> register(RegisterDTO registerDTO) {
        return null;
    }
}
