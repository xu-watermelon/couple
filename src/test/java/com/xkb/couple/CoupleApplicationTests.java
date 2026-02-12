package com.xkb.couple;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

import java.util.Random;


@SpringBootTest
class CoupleApplicationTests {

    @Autowired
    private JavaMailSender sender;

    @Test
    void sendMailTest() {
        SimpleMailMessage simpleMailMessage=new SimpleMailMessage();
        simpleMailMessage.setFrom("15839871769@163.com");
        simpleMailMessage.setTo("xuwatermelon@gmail.com");
        simpleMailMessage.setSubject("情侣小程序验证码");
        simpleMailMessage.setText("您的验证码为：123456");

        sender.send(simpleMailMessage);
        System.out.printf("发送成功");
    }
    @Test
    void generateCaptchaTest() {
        Random random = new Random();
        int captcha = random.nextInt(1000000);
        System.out.println(captcha);
    }

}
