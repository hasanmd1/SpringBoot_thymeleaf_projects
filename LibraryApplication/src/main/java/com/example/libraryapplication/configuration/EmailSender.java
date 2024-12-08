package com.example.libraryapplication.configuration;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
public class EmailSender {

    private final JavaMailSender javaMailSender;

    public EmailSender(JavaMailSender javaMailSender) {
        this.javaMailSender = javaMailSender;
    }

    public void sendVerification(String to, String verificationUrl) throws MailException, MessagingException {
        MimeMessage message  = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);

        helper.setTo(to);
        helper.setSubject("Email Verification");
        helper.setText("Please click the link below to verify your email address:\n" + verificationUrl, true);
        javaMailSender.send(message);

    }

    public void sendPasswordResetUrl(String to, String verificationUrl) throws MailException, MessagingException {
        MimeMessage message  = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);

        helper.setTo(to);
        helper.setSubject("Reset Password Url");
        helper.setText("Please click the link below to verify your email address:\n" + verificationUrl, true);
        javaMailSender.send(message);

    }
}
