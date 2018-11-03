package com.kmk.imageboard.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender javaMailSender;

    public void sendWelcomeEmail(String recipientEmail, String recipientUsername) {
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setFrom("rakenduscf@gmail.com");
        mailMessage.setTo(recipientEmail);
        mailMessage.setSubject("Your registration to Rakendus.cf was successful!");
        mailMessage.setText("Hey, " + recipientUsername + "!\n\nWe welcome You to our cool site!\n\nSincerely\nThe KMK team");
        javaMailSender.send(mailMessage);
    }
}
