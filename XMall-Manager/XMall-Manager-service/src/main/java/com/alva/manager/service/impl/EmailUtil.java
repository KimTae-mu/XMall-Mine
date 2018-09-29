package com.alva.manager.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMailMessage;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * <一句话描述>,
 * <详细介绍>,
 *
 * @author 穆国超
 * @since 设计wiki | 需求wiki
 */
public class EmailUtil {
    private static final Logger log = LoggerFactory.getLogger(EmailUtil.class);

    @Value("${EMAIL_HOST}")
    private String EMAIL_HOST;

    @Value("${EMAIL_USERNAME}")
    private String EMAIL_USERNAME;

    @Value("${EMAIL_PASSWORD}")
    private String EMAIL_PASSWORD;

    @Value("${EMAIL_SENDER}")
    private String EMAIL_SENDER;

    @Async
    public void sendEmailPayResult(String sendTo, String title, String content) {
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();

        //设定邮箱服务器配置
        mailSender.setHost(EMAIL_HOST);
        mailSender.setUsername(EMAIL_USERNAME);
        mailSender.setPassword(EMAIL_PASSWORD);

        //服务器进行验证
        Properties properties = new Properties();
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.timeout", "20000");
        //qq邮箱需开启
        properties.put("mail.smtp.ssl.enable", "true");
        //邮箱发送服务器端口,这里设置为465端口 避免服务器解封25端口
        properties.setProperty("mail.smtp.port", "465");
        properties.setProperty("mail.smtp.socketFactory.port", "465");
        properties.put("mail.smtp.auth", "true");

        mailSender.setJavaMailProperties(properties);

        //发送html邮件
        MimeMessage mimeMailMessage = mailSender.createMimeMessage();
        MimeMessageHelper mimeMessageHelper = null;
        //设置邮件内容
        try {
            mimeMessageHelper = new MimeMessageHelper(mimeMailMessage, true, "UTF-8");
            mimeMessageHelper.setTo(sendTo);
            mimeMessageHelper.setFrom(EMAIL_SENDER);
            mimeMessageHelper.setSubject(title);

            //true表示HTML格式的邮件
            mimeMessageHelper.setText(content, true);

        } catch (MessagingException e) {
            e.printStackTrace();
        }
        mailSender.send(mimeMailMessage);

        log.info("给 " + sendTo + " 的审核邮件发送成功!");
    }

    public static boolean checkEmail(String email) {
        boolean flag = false;
        try {
            String check = "^([a-z0-9A-Z]+[-|_|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$";
            Pattern pattern = Pattern.compile(check);
            Matcher matcher = pattern.matcher(email);
            flag = matcher.matches();
        } catch (Exception e) {
            flag = false;
        }
        return flag;
    }

}
