package com.xkb.couple.core.utils;

import com.xkb.couple.core.common.constants.ErrorCodeEnum;
import com.xkb.couple.core.common.constants.SystemConfigConstans;
import com.xkb.couple.core.common.exception.BusinessException;
import com.xkb.couple.core.config.EmailTemplateConfig;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import jakarta.mail.internet.MimeMessage;
import java.nio.charset.StandardCharsets;

/**
 * 邮件工具类
 * @author xuwatermelon
 * @date 2026/02/10
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class MailUtil {
    private final JavaMailSender javaMailSender;
    private final EmailTemplateConfig emailTemplateConfig;

    @Value("${couple.mail.fromEmail}")
    private String fromEmail;

    @Value("${couple.mail.fromName}")
    private String fromName;

    /**
     * 发送注册验证码邮件
     * @param to 收件人
     * @param code 验证码
     * @throws BusinessException 发送失败时抛出业务异常
     */
    public void sendRegisterMail(String to, String code) throws BusinessException {
        sendMail(to, code, SystemConfigConstans.CAPTCHA_TYPE_REGISTER);
    }

    /**
     * 发送忘记密码验证码邮件
     * @param to 收件人
     * @param code 验证码
     * @throws BusinessException 发送失败时抛出业务异常
     */
    public void sendForgetMail(String to, String code) throws BusinessException {
        sendMail(to, code, SystemConfigConstans.CAPTCHA_TYPE_FORGET);
    }

    /**
     * 发送登录验证码邮件
     * @param to 收件人
     * @param code 验证码
     * @throws BusinessException 发送失败时抛出业务异常
     */
    public void sendLoginMail(String to, String code) throws BusinessException {
        sendMail(to, code, SystemConfigConstans.CAPTCHA_TYPE_LOGIN);
    }

    /**
     * 发送邮件（通用方法）
     * @param to 收件人
     * @param code 验证码
     * @param templateType 模板类型
     * @throws BusinessException 发送失败时抛出业务异常
     */
    private void sendMail(String to, String code, String templateType) throws BusinessException {
        // 1. 参数校验
        if (to == null || to.isEmpty()) {
            log.error("邮件发送失败：收件人邮箱为空");
            throw new BusinessException(ErrorCodeEnum.EMAIL_EMPTY);
        }
        if (code == null || code.isEmpty()) {
            log.error("邮件发送失败：验证码为空");
            throw new BusinessException(ErrorCodeEnum.EMAIL_CONTENT_EMPTY);
        }

        try {
            // 2. 获取模板参数
            EmailTemplateConfig.TemplateParameters params = emailTemplateConfig.getParameters().get(templateType);
            if (params == null) {
                log.error("邮件发送失败：未找到模板参数，类型：{}", templateType);
                throw new BusinessException(ErrorCodeEnum.PARAMS_ERROR);
            }

            // 3. 生成邮件内容
            String htmlContent = String.format(
                    emailTemplateConfig.getCommon(),
                    params.getTitle(),
                    params.getCaptchaDesc(),
                    code,
                    params.getUsage()
            );

            // 4. 创建 MIME 邮件（支持HTML）
            MimeMessage mimeMessage = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, StandardCharsets.UTF_8.name());

            // 5. 设置标准邮件头
            // 发件人（确保编码正确）
            helper.setFrom(fromEmail, fromName);
            // 收件人
            helper.setTo(to);
            // 邮件主题
            helper.setSubject(params.getTitle());

            // 6. 设置邮件内容
            helper.setText(htmlContent, true);

            // 7. 发送邮件
            javaMailSender.send(mimeMessage);

            // 8. 记录成功日志（邮箱脱敏）
            log.info("{}邮件发送成功，收件人：{}", templateType, LogDesensitizeUtil.desensitizeEmail(to));

        } catch (BusinessException e) {
            // 重新抛出业务异常
            throw e;
        } catch (Exception e) {
            // 记录失败日志（邮箱脱敏，保留异常信息）
            log.error("邮件发送失败：to={}, templateType={}, error={}",
                    LogDesensitizeUtil.desensitizeEmail(to), templateType,
                    e.getMessage(), e);

            // 转换为业务异常
            throw new BusinessException(ErrorCodeEnum.MAIL_SEND_FAILED);
        }
    }
}