package com.xkb.couple.core.utils;

import com.xkb.couple.core.common.constants.ErrorCodeEnum;
import com.xkb.couple.core.common.exception.BusinessException;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import java.security.SecureRandom;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * 验证码工具类
 * @author xuwatermelon
 * @date 2026/02/10
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class CaptchaUtil {
    private final RedisTemplate<String, String> redisTemplate;

    /**
     * 验证码有效期（秒）TODO可以设置默认值
     */
    @Value("${couple.captcha.expiration}")
    private long captchaExpiration;

    /**
     * Redis键前缀
     */
    private static final String CAPTCHA_PREFIX = "captcha:";

    /**
     * 验证码长度
     */
    private static final int CAPTCHA_LENGTH = 6;



     /**
      * 验证码结果类，用于同时返回验证码ID和实际验证码
     */
     @Getter
    public static class CaptchaResult {
        private final String captchaId;
        private final String captcha;

        public CaptchaResult(String captchaId, String captcha) {
            this.captchaId = captchaId;
            this.captcha = captcha;
        }
    }

    /**
     * 生成验证码
     * @param email 收件人邮箱
     * @return 验证码ID（用于验证）
     * @throws BusinessException 生成失败时抛出业务异常
     */
    public CaptchaResult generateCaptcha(String email) {
        // 1. 参数校验
        if (email == null || email.isEmpty()) {
            log.error("验证码生成失败：邮箱为空");
            throw new BusinessException(ErrorCodeEnum.EMAIL_EMPTY);
        }

        try {
            // 2. 生成6位数字验证码（使用更安全的SecureRandom）
            SecureRandom random = new SecureRandom();
            int captchaInt = random.nextInt((int) Math.pow(10, CAPTCHA_LENGTH));
            // 确保验证码为6位数字（不足时补零）
            String captcha = String.format("%06d", captchaInt);

            // 3. 使用UUID作为Redis键，提高安全性
            String captchaId = UUID.randomUUID().toString();
            String redisKey = CAPTCHA_PREFIX + captchaId;

            // 4. 存储邮箱和验证码的组合
            String value = email + ":" + captcha;
            redisTemplate.opsForValue().set(redisKey, value, captchaExpiration, TimeUnit.SECONDS);

            log.info("验证码生成成功：email={}, captchaId={}",
                    LogDesensitizeUtil.desensitizeEmail(email), captchaId);

            return new CaptchaResult(captchaId, captcha);
        } catch (Exception e) {
            log.error("验证码生成失败：email={}, error={}",
                    LogDesensitizeUtil.desensitizeEmail(email), e.getMessage(), e);
            throw new BusinessException(ErrorCodeEnum.CAPTCHA_GENERATE_FAILED);
        }
    }

    /**
     * 验证验证码
     * @param captchaId 验证码ID
     * @param email 邮箱
     * @param captcha 验证码
     * @return 验证结果
     * @throws BusinessException 参数错误时抛出业务异常
     */
    public boolean validateCaptcha(String captchaId, String email, String captcha) throws BusinessException {
        // 1. 参数校验
        if (captchaId == null || captchaId.isEmpty()) {
            log.warn("验证码验证失败：验证码ID为空");
            throw new BusinessException(ErrorCodeEnum.CAPTCHA_ID_EMPTY);
        }
        if (email == null || email.isEmpty()) {
            log.warn("验证码验证失败：邮箱为空");
            throw new BusinessException(ErrorCodeEnum.EMAIL_EMPTY);
        }
        if (captcha == null || captcha.isEmpty()) {
            log.warn("验证码验证失败：验证码为空");
            throw new BusinessException(ErrorCodeEnum.CAPTCHA_EMPTY);
        }

        try {
            // 2. 构建Redis键
            String redisKey = CAPTCHA_PREFIX + captchaId;

            // 3. 获取并删除验证码（原子操作，防止重复使用）
            String storedValue = redisTemplate.opsForValue().getAndDelete(redisKey);

            if (storedValue == null) {
                log.warn("验证码验证失败：验证码不存在或已过期，captchaId={}", captchaId);
                return false;
            }

            // 4. 解析存储的内容
            String[] parts = storedValue.split(":", 2);
            if (parts.length != 2) {
                log.error("验证码验证失败：存储格式错误，storedValue={}", storedValue);
                return false;
            }

            String storedEmail = parts[0];
            String storedCaptcha = parts[1];

            // 5. 验证邮箱和验证码
            boolean isValid = storedEmail.equals(email) && storedCaptcha.equals(captcha);

            if (isValid) {
                log.info("验证码验证成功：email={}, captchaId={}",
                        LogDesensitizeUtil.desensitizeEmail(email), captchaId);
            } else {
                log.warn("验证码验证失败：邮箱或验证码不匹配，email={}, captchaId={}",
                        LogDesensitizeUtil.desensitizeEmail(email), captchaId);
            }

            return isValid;
        } catch (BusinessException e) {
            // 重新抛出业务异常
            throw e;
        } catch (Exception e) {
            log.error("验证码验证失败：captchaId={}, email={}, error={}",
                    captchaId, LogDesensitizeUtil.desensitizeEmail(email), e.getMessage(), e);
            return false;
        }
    }
}