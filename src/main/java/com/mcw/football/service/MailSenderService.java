/*
Created by IntelliJ IDEA.
By trohi
Date: 15.05.2019 15:06
Project Name: football
*/
package com.mcw.football.service;

import org.springframework.stereotype.Service;

@Service
public interface MailSenderService {

    void send(String emailTo, String subject, String message);
}
