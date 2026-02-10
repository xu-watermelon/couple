package com.xkb.couple.core.utils;

import com.xkb.couple.core.common.constants.ErrorCodeEnum;
import com.xkb.couple.core.common.exception.BusinessException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

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
    
    @Value("${couple.mail.from}")
    private String fromEmail;


    
    /**
     * 发送验证码邮件
     * @param to 收件人
     * @param content 邮件内容
     * @throws BusinessException 发送失败时抛出业务异常
     */
    public void sendMail(String to, String content) throws BusinessException {
        // 1. 参数校验
        if (to == null || to.isEmpty()) {
            log.error("邮件发送失败：收件人邮箱为空");
            throw new BusinessException(ErrorCodeEnum.EMAIL_EMPTY);
        }
        if (content == null || content.isEmpty()) {
            log.error("邮件发送失败：邮件内容为空");
            throw new BusinessException(ErrorCodeEnum.EMAIL_CONTENT_EMPTY);
        }
        
        try {
            // 2. 创建邮件消息
            SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
            simpleMailMessage.setFrom(fromEmail);
            simpleMailMessage.setTo(to);
            String subject = "验证码";
            simpleMailMessage.setSubject(subject);
            simpleMailMessage.setText(content);
            
            // 3. 发送邮件
            javaMailSender.send(simpleMailMessage);
            
            // 4. 记录成功日志（邮箱脱敏）
            log.info("验证码邮件发送成功：from={}, to={}", 
                    LogDesensitizeUtil.desensitizeEmail(fromEmail),
                    LogDesensitizeUtil.desensitizeEmail(to));
        } catch (Exception e) {
            // 5. 记录失败日志（邮箱脱敏，保留异常信息）
            log.error("验证码邮件发送失败：from={}, to={}, error={}", 
                    LogDesensitizeUtil.desensitizeEmail(fromEmail),
                    LogDesensitizeUtil.desensitizeEmail(to),
                    e.getMessage(), e);
            
            // 6. 转换为业务异常
            throw new BusinessException(ErrorCodeEnum.MAIL_SEND_ERROR);
        }
    }
}