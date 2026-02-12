package com.xkb.couple.core.utils;

import com.xkb.couple.core.common.constants.ErrorCodeEnum;
import com.xkb.couple.core.common.exception.BusinessException;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

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
    
    @Value("${couple.mail.fromEmail}")
    private String fromEmail;

    @Value("${couple.mail.fromName}")
    private  String fromName;
    private final String RegisterHtmlTemplate = "<div style='font-family: Arial, sans-serif; max-width: 600px; margin: 0 auto; padding: 20px;'>" +
            "<h2 style='color: #333;'>欢迎注册情侣小程序平台</h2>" +
            "<p style='font-size: 16px; line-height: 1.6;'>您好！</p>" +
            "<p style='font-size: 16px; line-height: 1.6;'>您的注册验证码为：<strong style='font-size: 20px; color: #0066cc;'>%s</strong></p>" +
            "<p style='font-size: 14px; color: #666;'>验证码有效期为5分钟，请尽快完成验证。</p>" +
            "<p style='font-size: 12px; color: #999;'>【安全提示】验证码仅用于注册验证，请勿泄露给他人；如非本人操作，请忽略此邮件。</p>" +
            "<p style='font-size: 12px; color: #999;'>情侣小程序平台 客服热线：400-XXXX-XXXX</p>" +
            "</div>";
    private final String  ForgetPasswordHtmlTemplate = "<div style='font-family: Arial, sans-serif; max-width: 600px; margin: 0 auto; padding: 20px;'>" +
            "<h2 style='color: #333;'>找回密码</h2>" +
            "<p style='font-size: 16px; line-height: 1.6;'>您好！</p>" +
            "<p style='font-size: 16px; line-height: 1.6;'>您的注册验证码为：<strong style='font-size: 20px; color: #0066cc;'>%s</strong></p>" +
            "<p style='font-size: 14px; color: #666;'>验证码有效期为5分钟，请尽快完成验证。</p>" +
            "<p style='font-size: 12px; color: #999;'>【安全提示】验证码仅用于找回密码验证，请勿泄露给他人；如非本人操作，请忽略此邮件。</p>" +
            "<p style='font-size: 12px; color: #999;'>情侣小程序平台 客服热线：400-XXXX-XXXX</p>" +
            "</div>";


    /**
     * 发送注册验证码邮件
     */
    public void sendRegisterMail(String to, String code) throws BusinessException {
        sendMail(to, code,RegisterHtmlTemplate);
    }
    /**
     * 发送找回密码邮件
     */
    public void sendForgetPasswordMail(String to, String code) throws BusinessException {
        sendMail(to, code,ForgetPasswordHtmlTemplate);
    }



    /**
     * 发送验证码邮件
     * @param to 收件人
     * @param code 验证码
     * @throws BusinessException 发送失败时抛出业务异常
     */
    public void sendMail(String to, String code,String template) throws BusinessException {
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
            // 2. 创建 MIME 邮件（支持HTML）
            MimeMessage mimeMessage = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, StandardCharsets.UTF_8.name());

            // 3. 设置标准邮件头
            // 发件人（带名称，更规范）
            helper.setFrom(new InternetAddress(fromEmail, fromName, StandardCharsets.UTF_8.name()));
            // 收件人
            helper.setTo(to);
            // 邮件主题（规范：平台+用途）
            helper.setSubject("【情侣小程序平台】注册验证码");

            // 4. 填充正文
            String htmlContent = String.format(template, code);
            // true 表示正文是HTML格式
            helper.setText(htmlContent, true);

            // 5. 发送邮件
            javaMailSender.send(mimeMessage);
            log.info("注册验证码邮件发送成功，收件人：{}", LogDesensitizeUtil.desensitizeEmail(to));

        } catch (Exception e) {
            log.error("注册验证码邮件发送失败，收件人：{}", LogDesensitizeUtil.desensitizeEmail(to), e);
            throw new BusinessException(ErrorCodeEnum.MAIL_SEND_FAILED);
        }
    }
}