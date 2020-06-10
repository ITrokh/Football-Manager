package com.mcw.football.service.impl;

import com.mcw.football.service.MailSenderService;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MailSenderServiceImpl implements MailSenderService {

    private final JavaMailSender mailSender;

    @Override
    public void send(String emailTo, String subject, String message) {
        SimpleMailMessage mailMessage = new SimpleMailMessage();

        mailMessage.setTo(emailTo);
        mailMessage.setSubject(subject);
        mailMessage.setText(message);

        mailSender.send(mailMessage);
    }
}
