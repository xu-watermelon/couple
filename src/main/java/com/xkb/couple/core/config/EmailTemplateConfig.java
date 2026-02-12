package com.xkb.couple.core.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * 邮件模板配置
 */
@Data
@Component
@ConfigurationProperties(prefix = "couple.mail.templates")
public class EmailTemplateConfig {
    /**
     * 通用邮件模板
     */
    private String common;

    /**
     * 模板参数
     */
    private Map<String, TemplateParameters> parameters;

    /**
     * 模板参数类
     */
    @Data
    public static class TemplateParameters {
        private String title;
        private String captchaDesc;
        private String usage;
    }
}