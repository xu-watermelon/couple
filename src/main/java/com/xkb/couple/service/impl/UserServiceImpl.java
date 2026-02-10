package com.xkb.couple.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.xkb.couple.core.common.constants.ErrorCodeEnum;
import com.xkb.couple.core.common.exception.BusinessException;
import com.xkb.couple.core.common.resp.BaseResponse;
import com.xkb.couple.core.constants.UserConstans;
import com.xkb.couple.core.utils.JwtUtil;
import com.xkb.couple.core.utils.LogDesensitizeUtil;
import com.xkb.couple.core.utils.UserHolder;
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

import java.time.LocalDateTime;

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
            return BaseResponse.fail(ErrorCodeEnum.USERNAME_OR_PASSWORD_EMPTY);
        }
        // 2. 查询用户
        User user = userMapper.selectOne(new LambdaQueryWrapper<User>()
                .eq(User::getUsername, loginDTO.getUsername()));
        if (user == null) {
            log.info("用户登录失败(用户不存在)：username={}", LogDesensitizeUtil.desensitizeUsername(loginDTO.getUsername()));
            return BaseResponse.fail(ErrorCodeEnum.USER_NOT_EXIST);
        }
        // 3. 校验密码(md5)
        // 密码md5加密
        String password = DigestUtils.md5DigestAsHex(loginDTO.getPassword().getBytes());
        if (!user.getPassword().equals(password)) {
            log.info("用户登录失败(密码错误)：username={}, password={}", LogDesensitizeUtil.desensitizeUsername(loginDTO.getUsername()), LogDesensitizeUtil.desensitizePassword(loginDTO.getPassword()));
            return BaseResponse.fail(ErrorCodeEnum.USER_PASSWORD_ERROR);
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
    public BaseResponse<UserVO> register(RegisterDTO registerDTO) {
        // 1. 校验参数
        if (registerDTO.getUsername() == null || registerDTO.getPassword() == null || registerDTO.getConfirmPassword() == null) {
            log.info("用户注册失败(用户名或密码为空)：username={}, password={}", LogDesensitizeUtil.desensitizeUsername(registerDTO.getUsername()), LogDesensitizeUtil.desensitizePassword(registerDTO.getPassword()));
            return BaseResponse.fail(ErrorCodeEnum.USERNAME_OR_PASSWORD_EMPTY);
        }
        // 2. 校验密码是否一致
        if (!registerDTO.getPassword().equals(registerDTO.getConfirmPassword())) {
            log.info("用户注册失败(密码不一致)：username={}, password={}, confirmPassword={}", LogDesensitizeUtil.desensitizeUsername(registerDTO.getUsername()), LogDesensitizeUtil.desensitizePassword(registerDTO.getPassword()), LogDesensitizeUtil.desensitizePassword(registerDTO.getConfirmPassword()));
            return BaseResponse.fail(ErrorCodeEnum.PASSWORD_NOT_MATCH);
        }
        //3.查询数据库中是否存在该用户
        User user = userMapper.selectOne(new LambdaQueryWrapper<User>().eq(User::getUsername, registerDTO.getUsername()));
        if (user != null) {
            log.info("用户注册失败(用户名已存在)：username={}", LogDesensitizeUtil.desensitizeUsername(registerDTO.getUsername()));
            return BaseResponse.fail(ErrorCodeEnum.USER_EXIST);
        }
        //4.将用户插入数据库TODO 差一个头像上传接口
        userMapper.insert(User.builder()
                .username(registerDTO.getUsername())
                .password(DigestUtils.md5DigestAsHex(registerDTO.getPassword().getBytes()))
                .nickname(registerDTO.getNickname())
                .avatar(registerDTO.getAvatar())
                .hasCouple(UserConstans.UserEnum.FEMALE.getValue())
                .gender(registerDTO.getGender())
                .birthday(registerDTO.getBirthday())
                .createTime(LocalDateTime.now())
                .updateTime(LocalDateTime.now())
                .build());
        user = userMapper.selectOne(new LambdaQueryWrapper<User>().eq(User::getUsername, registerDTO.getUsername()));
        log.info("用户注册成功：username={}", LogDesensitizeUtil.desensitizeUsername(registerDTO.getUsername()));
        UserVO userVO = UserVO.builder()
                .id(user.getId())
                .nickname(user.getNickname())
                .username(user.getUsername())
                .hasCouple(user.getHasCouple())
                .avatar(user.getAvatar())
                .gender(user.getGender())
                .birthday(user.getBirthday())
                .build();

        return BaseResponse.success(userVO);


    }


    @Override
    public BaseResponse<UserVO> getUserInfo(Long id) {
        //1. 校验参数
        if (id == null) {
            log.info("用户查询失败(用户id为空)：id={}", id);
            return BaseResponse.fail(ErrorCodeEnum.USER_ID_EMPTY);
        }
       Long userId = UserHolder.getUserId();
        if (userId == null) {
            log.info("用户查询失败(用户未登录)：id={}", id);
            return BaseResponse.fail(ErrorCodeEnum.USER_NOT_LOGIN);
        }
        if (!userId.equals(id)) {
            log.info("用户查询失败(用户无权限)：id={}", id);
            throw  new BusinessException(ErrorCodeEnum.USER_NOT_AUTHORIZED);
        }

        //2. 查询用户
        User user = userMapper.selectOne(new LambdaQueryWrapper<User>().eq(User::getId, id));
        if (user == null) {
            log.info("用户查询失败(用户不存在)：id={}", id);
            return BaseResponse.fail(ErrorCodeEnum.USER_NOT_EXIST);
        }
        //3. 构建用户VO
        UserVO userVO = UserVO.builder()
                .id(user.getId())
                .nickname(user.getNickname())
                .username(user.getUsername())
                .hasCouple(user.getHasCouple())
                .avatar(user.getAvatar())
                .gender(user.getGender())
                .birthday(user.getBirthday())
                .build();
        return BaseResponse.success(userVO);
    }
}


