package com.xkb.couple.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.xkb.couple.core.common.constants.ErrorCodeEnum;
import com.xkb.couple.core.common.constants.SystemConfigConstans;
import com.xkb.couple.core.common.exception.BusinessException;
import com.xkb.couple.core.common.resp.BaseResponse;
import com.xkb.couple.core.constants.UserConstans;
import com.xkb.couple.core.utils.*;
import com.xkb.couple.mapper.UserMapper;
import com.xkb.couple.pojo.dto.*;
import com.xkb.couple.pojo.entity.User;
import com.xkb.couple.pojo.vo.LoginResponseVO;
import com.xkb.couple.pojo.vo.UserVO;
import com.xkb.couple.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
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
    private final MailUtil mailUtil;
    private final CaptchaUtil captchaUtil;

    // BCrypt 密码编码器
    private static final BCryptPasswordEncoder  bcrypt = new BCryptPasswordEncoder();


    @Override
    public BaseResponse<LoginResponseVO> login(PasswordLoginDTO passwordLoginDTO) {
        // 1. 校验参数
        if (passwordLoginDTO.getEmail() == null || passwordLoginDTO.getPassword() == null) {
            log.info("用户登录失败(邮箱或密码为空)：email={}, password={}", LogDesensitizeUtil.desensitizeEmail(passwordLoginDTO.getEmail()), LogDesensitizeUtil.desensitizePassword(passwordLoginDTO.getPassword()));
            return BaseResponse.fail(ErrorCodeEnum.EMAIL_OR_PASSWORD_EMPTY);
        }
        // 2. 查询用户
        User user = userMapper.selectOne(new LambdaQueryWrapper<User>()
                .eq(User::getEmail, passwordLoginDTO.getEmail()));
        if (user == null) {
            log.info("用户登录失败(用户不存在)：email={}", LogDesensitizeUtil.desensitizeEmail(passwordLoginDTO.getEmail()));
            return BaseResponse.fail(ErrorCodeEnum.USER_NOT_EXIST);
        }
        // 3. 校验密码(bcrypt)
        // 密码bcrypt加密
        if (!bcrypt.matches(passwordLoginDTO.getPassword(), user.getPassword())) {
            log.info("用户登录失败(密码错误)：email={}, password={}", LogDesensitizeUtil.desensitizeEmail(passwordLoginDTO.getEmail()), LogDesensitizeUtil.desensitizePassword(passwordLoginDTO.getPassword()));
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

        log.info("用户登录成功：email={}, token={}", LogDesensitizeUtil.desensitizeEmail(passwordLoginDTO.getEmail()), LogDesensitizeUtil.desensitizeToken(token));
        LoginResponseVO loginResponseVO = LoginResponseVO.builder()
                .token(token)
                .userVO(userVO)
                .build();
        return BaseResponse.success(loginResponseVO);
    }


    @Override
    public BaseResponse<LoginResponseVO> loginByCaptcha(CaptchaLoginDTO captchaLoginDTO) {
        // 1. 校验参数
        if (captchaLoginDTO.getEmail() == null) {
            log.info("用户登录失败(邮箱为空)：email={}", LogDesensitizeUtil.desensitizeEmail(null));
            return BaseResponse.fail(ErrorCodeEnum.EMAIL_EMPTY);
        }
        if (captchaLoginDTO.getCaptcha() == null || captchaLoginDTO.getCaptchaId() == null) {
            log.info("用户登录失败(验证码或验证码id为空)：email={}, captcha={}, captchaId={}", LogDesensitizeUtil.desensitizeEmail(captchaLoginDTO.getEmail()), LogDesensitizeUtil.desensitizeCaptcha(captchaLoginDTO.getCaptcha()),captchaLoginDTO.getCaptchaId());
            return BaseResponse.fail(ErrorCodeEnum.CAPTCHA_EMPTY);
        }
        //2. 验证码校验
        if (!captchaUtil.validateCaptcha(captchaLoginDTO.getCaptchaId(), captchaLoginDTO.getEmail(), captchaLoginDTO.getCaptcha())) {
            log.info("用户登录失败(验证码错误)：email={}, captcha={}", LogDesensitizeUtil.desensitizeEmail(captchaLoginDTO.getEmail()), LogDesensitizeUtil.desensitizeCaptcha(captchaLoginDTO.getCaptcha()));
            return BaseResponse.fail(ErrorCodeEnum.CAPTCHA_ERROR);
        }
        // 3. 查询用户
        User user = userMapper.selectOne(new LambdaQueryWrapper<User>()
                .eq(User::getEmail, captchaLoginDTO.getEmail()));
        if (user == null) {
            log.info("用户登录失败(用户不存在)：email={}", LogDesensitizeUtil.desensitizeEmail(captchaLoginDTO.getEmail()));
            return BaseResponse.fail(ErrorCodeEnum.USER_NOT_EXIST);
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
        log.info("用户登录成功：email={}, token={}", LogDesensitizeUtil.desensitizeEmail(captchaLoginDTO.getEmail()), LogDesensitizeUtil.desensitizeToken(token));
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
                .password(bcrypt.encode(registerDTO.getPassword()))
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


    @Override
    public BaseResponse<String> getCaptcha(String email, String type) {

            if (email == null) {
                log.info("获取验证码失败(邮箱为空)：email={}", (Object) null);
                return BaseResponse.fail(ErrorCodeEnum.EMAIL_EMPTY);
            }

            // 根据类型生成不同的验证码
            CaptchaUtil.CaptchaResult captchaResult = captchaUtil.generateCaptcha(email);

            // 根据类型发送不同的邮件
        switch (type) {
            case SystemConfigConstans.CAPTCHA_TYPE_REGISTER ->
                    mailUtil.sendRegisterMail(email, captchaResult.getCaptcha());
            case SystemConfigConstans.CAPTCHA_TYPE_FORGET -> mailUtil.sendForgetMail(email, captchaResult.getCaptcha());
            case SystemConfigConstans.CAPTCHA_TYPE_LOGIN -> mailUtil.sendLoginMail(email, captchaResult.getCaptcha());
            case null, default -> {
                log.error("获取验证码失败(类型错误)：type={}", type);
                return BaseResponse.fail(ErrorCodeEnum.PARAMS_ERROR);
            }
        }

            return BaseResponse.success(captchaResult.getCaptchaId());

    }


    @Override
    public BaseResponse<Void> forgetPassword(ForgetPasswordDTO forgetPasswordDTO) {
        if (forgetPasswordDTO == null) {
            log.info("忘记密码失败(参数为空)：forgetPasswordDTO={}", (Object) null);
            return BaseResponse.fail(ErrorCodeEnum.PARAMS_ERROR);
        }
        if (forgetPasswordDTO.getEmail() == null) {
            log.info("忘记密码失败(邮箱为空)：email={}", (Object) null);
            return BaseResponse.fail(ErrorCodeEnum.EMAIL_EMPTY);
        }
        if (forgetPasswordDTO.getCaptcha() == null) {
            log.info("忘记密码失败(验证码为空)：captcha={}", (Object) null);
            return BaseResponse.fail(ErrorCodeEnum.CAPTCHA_EMPTY);
        }
        if (forgetPasswordDTO.getCaptchaId() == null) {
            log.info("忘记密码失败(验证码ID为空)：captchaId={}", (Object) null);
            return BaseResponse.fail(ErrorCodeEnum.CAPTCHA_ID_EMPTY);
        }
        if (forgetPasswordDTO.getPassword() == null) {
            log.info("忘记密码失败(密码为空)：password={}", (Object) null);
            return BaseResponse.fail(ErrorCodeEnum.EMAIL_OR_PASSWORD_EMPTY);
        }
        if (!forgetPasswordDTO.getPassword().equals(forgetPasswordDTO.getConfirmPassword())) {
            log.info("忘记密码失败(两次密码不一致)：password={}, confirmPassword={}", LogDesensitizeUtil.desensitizePassword(forgetPasswordDTO.getPassword()), LogDesensitizeUtil.desensitizePassword(forgetPasswordDTO.getConfirmPassword()));
            return BaseResponse.fail(ErrorCodeEnum.PASSWORD_NOT_MATCH);
        }
        if (!captchaUtil.validateCaptcha(forgetPasswordDTO.getCaptchaId(), forgetPasswordDTO.getEmail(), forgetPasswordDTO.getCaptcha())) {
            log.info("忘记密码失败(验证码错误)：captchaId={}, email={}, captcha={}", forgetPasswordDTO.getCaptchaId(), LogDesensitizeUtil.desensitizeEmail(forgetPasswordDTO.getEmail()), LogDesensitizeUtil.desensitizeCaptcha(forgetPasswordDTO.getCaptcha()));
            return BaseResponse.fail(ErrorCodeEnum.CAPTCHA_ERROR);
        }
        User user = userMapper.selectOne(new LambdaQueryWrapper<User>().eq(User::getEmail, forgetPasswordDTO.getEmail()));
        if (user == null) {
            log.info("忘记密码失败(用户不存在)：email={}", LogDesensitizeUtil.desensitizeEmail(forgetPasswordDTO.getEmail()));
            return BaseResponse.fail(ErrorCodeEnum.USER_NOT_EXIST);
        }
        // 更新用户密码
        user.setPassword(bcrypt.encode(forgetPasswordDTO.getPassword()));
        user.setUpdateTime(LocalDateTime.now());
        userMapper.updateById(user);
        return BaseResponse.success();
    }


    @Override
    public BaseResponse<Void> updatePassword(UpdatePasswordDTO updatePasswordDTO) {
        // 1.参数校验
        // 校验参数是否为空
        if (updatePasswordDTO == null) {
            log.info("修改密码失败(参数为空)：updatePasswordDTO={}", (Object) null);
            return BaseResponse.fail(ErrorCodeEnum.PARAMS_ERROR);
        }
        // 校验密码是否为空
       if (updatePasswordDTO.getOldPassword() == null || updatePasswordDTO.getNewPassword() == null || updatePasswordDTO.getConfirmNewPassword() == null) {
           log.info("重置密码失败(密码为空)：oldPassword={}, newPassword={}, confirmNewPassword={}", LogDesensitizeUtil.desensitizePassword(updatePasswordDTO.getOldPassword()), LogDesensitizeUtil.desensitizePassword(updatePasswordDTO.getNewPassword()), LogDesensitizeUtil.desensitizePassword(updatePasswordDTO.getConfirmNewPassword()));
           return BaseResponse.fail(ErrorCodeEnum.EMAIL_EMPTY);
       }
       //检验旧密码和新密码是否一致
        if (updatePasswordDTO.getOldPassword().equals(updatePasswordDTO.getNewPassword())) {
            log.info("重置密码失败(旧密码和新密码一致)：oldPassword={}, newPassword={}", LogDesensitizeUtil.desensitizePassword(updatePasswordDTO.getOldPassword()), LogDesensitizeUtil.desensitizePassword(updatePasswordDTO.getNewPassword()));
            return BaseResponse.fail(ErrorCodeEnum.OLD_PASSWORD_EQUAL_NEW_PASSWORD);
        }
        // 检验新密码和确认新密码是否一致
       if (!updatePasswordDTO.getNewPassword().equals(updatePasswordDTO.getConfirmNewPassword())) {
           log.info("重置密码失败(两次密码不一致)：newPassword={}, confirmNewPassword={}", LogDesensitizeUtil.desensitizePassword(updatePasswordDTO.getNewPassword()), LogDesensitizeUtil.desensitizePassword(updatePasswordDTO.getConfirmNewPassword()));
           return BaseResponse.fail(ErrorCodeEnum.PASSWORD_NOT_MATCH);
       }
       // 2.校验旧密码是否正确
       User user = userMapper.selectOne(new LambdaQueryWrapper<User>().eq(User::getId,UserHolder.getUserId()));
       if (user == null) {
           log.info("重置密码失败(用户不存在)：userId={}", UserHolder.getUserId());
           return BaseResponse.fail(ErrorCodeEnum.USER_NOT_EXIST);
       }
       //检验旧密码是否正确
       if (!bcrypt.matches(updatePasswordDTO.getOldPassword(), user.getPassword())) {
           log.info("重置密码失败(旧密码错误)：oldPassword={}", LogDesensitizeUtil.desensitizePassword(updatePasswordDTO.getOldPassword()));
           return BaseResponse.fail(ErrorCodeEnum.USER_PASSWORD_ERROR);
       }
       user.setPassword(bcrypt.encode(updatePasswordDTO.getNewPassword()));
       user.setUpdateTime(LocalDateTime.now());
       userMapper.updateById(user);
        return BaseResponse.success();
    }


}


