/*
Created by IntelliJ IDEA.
By trohi
Date: 15.05.2019 15:06
Project Name: football
*/
package com.mcw.football.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public interface MailSender {

    public void send(String emailTo, String subject, String message);
}
