package com.xkb.couple.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.xkb.couple.core.common.constants.ErrorCodeEnum;
import com.xkb.couple.core.common.constants.SystemConfigConstans;
import com.xkb.couple.core.common.exception.BusinessException;
import com.xkb.couple.core.common.resp.BaseResponse;
import com.xkb.couple.core.constants.UserConstans;
import com.xkb.couple.core.utils.*;
import com.xkb.couple.mapper.UserMapper;
import com.xkb.couple.pojo.dto.LoginDTO;
import com.xkb.couple.pojo.dto.RegisterDTO;
import com.xkb.couple.pojo.entity.User;
import com.xkb.couple.pojo.vo.LoginResponseVO;
import com.xkb.couple.pojo.vo.UserVO;
import com.xkb.couple.service.UserService;
import jakarta.validation.constraints.Email;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import java.time.LocalDateTime;
import java.util.regex.Pattern;

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
    private final MailUtil mailUtil;
    private final CaptchaUtil captchaUtil;

    @Override
    public BaseResponse<LoginResponseVO> login(LoginDTO loginDTO) {
        // 1. 校验参数
        if (loginDTO.getEmail() == null || loginDTO.getPassword() == null) {
            log.info("用户登录失败(邮箱或密码为空)：email={}, password={}", LogDesensitizeUtil.desensitizeEmail(loginDTO.getEmail()), LogDesensitizeUtil.desensitizePassword(loginDTO.getPassword()));
            return BaseResponse.fail(ErrorCodeEnum.EMAIL_OR_PASSWORD_EMPTY);
        }
        // 2. 查询用户
        User user = userMapper.selectOne(new LambdaQueryWrapper<User>()
                .eq(User::getEmail, loginDTO.getEmail()));
        if (user == null) {
            log.info("用户登录失败(用户不存在)：email={}", LogDesensitizeUtil.desensitizeEmail(loginDTO.getEmail()));
            return BaseResponse.fail(ErrorCodeEnum.USER_NOT_EXIST);
        }
        // 3. 校验密码(md5)
        // 密码md5加密
        String password = DigestUtils.md5DigestAsHex(loginDTO.getPassword().getBytes());
        if (!user.getPassword().equals(password)) {
            log.info("用户登录失败(密码错误)：email={}, password={}", LogDesensitizeUtil.desensitizeEmail(loginDTO.getEmail()), LogDesensitizeUtil.desensitizePassword(loginDTO.getPassword()));
            return BaseResponse.fail(ErrorCodeEnum.USER_PASSWORD_ERROR);
        }
        // 4. 生成token
        String token = jwtUtil.generateToken(user.getId());
        UserVO userVO = UserVO.builder()
                .id(user.getId())
                .nickname(user.getNickname())
                .email(user.getEmail())
                .hasCouple(user.getHasCouple())
                .avatar(user.getAvatar())
                .gender(user.getGender())
                .birthday(user.getBirthday())
                .build();

        log.info("用户登录成功：email={}, token={}", LogDesensitizeUtil.desensitizeEmail(loginDTO.getEmail()), LogDesensitizeUtil.desensitizeToken(token));
        LoginResponseVO loginResponseVO = LoginResponseVO.builder()
                .token(token)
                .userVO(userVO)
                .build();
        return BaseResponse.success(loginResponseVO);
    }

    @Override
    public BaseResponse<LoginResponseVO> register(RegisterDTO registerDTO) {
        // 1. 校验参数
//            1.检查验证码
        if (!captchaUtil.validateCaptcha(registerDTO.getCaptchaId(), registerDTO.getEmail(), registerDTO.getCaptcha())) {
            log.info("用户注册失败(验证码错误)：email={}, captcha={}", LogDesensitizeUtil.desensitizeEmail(registerDTO.getEmail()), LogDesensitizeUtil.desensitizeCaptcha(registerDTO.getCaptcha()));
            return BaseResponse.fail(ErrorCodeEnum.CAPTCHA_ERROR);
        }
        // 2. 校验密码是否一致
        if (!registerDTO.getPassword().equals(registerDTO.getConfirmPassword())) {
            log.info("用户注册失败(密码不一致)：email={}, password={}, confirmPassword={}", LogDesensitizeUtil.desensitizeEmail(registerDTO.getEmail()), LogDesensitizeUtil.desensitizePassword(registerDTO.getPassword()), LogDesensitizeUtil.desensitizePassword(registerDTO.getConfirmPassword()));
            return BaseResponse.fail(ErrorCodeEnum.PASSWORD_NOT_MATCH);
        }
        //3.查询数据库中是否存在该用户
        User user = userMapper.selectOne(new LambdaQueryWrapper<User>().eq(User::getEmail, registerDTO.getEmail()));
        if (user != null) {
            log.info("用户注册失败(邮箱已存在)：email={}", LogDesensitizeUtil.desensitizeEmail(registerDTO.getEmail()));
            return BaseResponse.fail(ErrorCodeEnum.USER_EXIST);
        }
        //4.将用户插入数据库TODO 差一个头像上传接口
        userMapper.insert(User.builder()
                .password(DigestUtils.md5DigestAsHex(registerDTO.getPassword().getBytes()))
                .nickname(registerDTO.getNickname())
                .avatar(registerDTO.getAvatar())
                .hasCouple(UserConstans.UserEnum.FEMALE.getValue())
                .gender(registerDTO.getGender())
                .email(registerDTO.getEmail())
                .birthday(registerDTO.getBirthday())
                .createTime(LocalDateTime.now())
                .updateTime(LocalDateTime.now())
                .build());
        user = userMapper.selectOne(new LambdaQueryWrapper<User>().eq(User::getEmail, registerDTO.getEmail()));
        log.info("用户注册成功：email={}", LogDesensitizeUtil.desensitizeEmail(registerDTO.getEmail()));
        // 4. 生成token
        String token = jwtUtil.generateToken(user.getId());
        UserVO userVO = UserVO.builder()
                .id(user.getId())
                .nickname(user.getNickname())
                .email(user.getEmail())
                .hasCouple(user.getHasCouple())
                .avatar(user.getAvatar())
                .gender(user.getGender())
                .birthday(user.getBirthday())
                .build();

        log.info("用户注册并登录成功：email={}, token={}", LogDesensitizeUtil.desensitizeEmail(registerDTO.getEmail()), LogDesensitizeUtil.desensitizeToken(token));
        LoginResponseVO loginResponseVO = LoginResponseVO.builder()
                .token(token)
                .userVO(userVO)
                .build();
        return BaseResponse.success(loginResponseVO);


    }


    @Override
    public BaseResponse<UserVO> getUserInfo(Long id) {
        //1. 校验参数
        if (id == null) {
            log.info("用户查询失败(用户id为空)：id={}", (Object) null);
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
                .hasCouple(user.getHasCouple())
                .avatar(user.getAvatar())
                .gender(user.getGender())
                .email(user.getEmail())
                .birthday(user.getBirthday())
                .build();
        return BaseResponse.success(userVO);
    }

    /**
     * @param email 邮箱
     * @return
     */
    @Override
    public BaseResponse<String> getRegisterCaptcha( String email) {
        // 1. 空值检查（必要）
        if (email == null || email.isEmpty()) {
            log.info("获取验证码失败(邮箱为空)");
            return BaseResponse.fail(ErrorCodeEnum.EMAIL_EMPTY);
        }

        // 2. 邮箱格式检查（可选，如果Controller层已验证）
        if (!Pattern.matches(SystemConfigConstans.EMAIL_REGEX, email)) {
            log.info("获取验证码失败(邮箱格式错误)：email={}", LogDesensitizeUtil.desensitizeEmail(email));
            return BaseResponse.fail(ErrorCodeEnum.EMAIL_FORMAT_ERROR);
        }
        CaptchaUtil.CaptchaResult captchaResult = captchaUtil.generateCaptcha(email);
        mailUtil.sendRegisterMail(email, captchaResult.getCaptcha());
        return BaseResponse.success(captchaResult.getCaptchaId());
    }

    /**
     * @param email 邮箱
     * @return
     */
    @Override
    public BaseResponse<String> getForgetCaptcha(String email) {
        // 1. 空值检查（必要）
        if (email == null || email.isEmpty()) {
            log.info("获取验证码失败(邮箱为空)");
            return BaseResponse.fail(ErrorCodeEnum.EMAIL_EMPTY);
        }

        // 2. 邮箱格式检查（可选，如果Controller层已验证）
        if (!Pattern.matches(SystemConfigConstans.EMAIL_REGEX, email)) {
            log.info("获取验证码失败(邮箱格式错误)：email={}", LogDesensitizeUtil.desensitizeEmail(email));
            return BaseResponse.fail(ErrorCodeEnum.EMAIL_FORMAT_ERROR);
        }
        CaptchaUtil.CaptchaResult captchaResult = captchaUtil.generateCaptcha(email);
        mailUtil.sendForgetMail(email, captchaResult.getCaptcha());
        return BaseResponse.success(captchaResult.getCaptchaId());
    }


}


