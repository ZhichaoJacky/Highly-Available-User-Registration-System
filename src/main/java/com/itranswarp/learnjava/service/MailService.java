package com.itranswarp.learnjava.service;

import java.time.LocalDateTime;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import com.itranswarp.learnjava.entity.User;

@Component
public class MailService{
	@Autowired
	JavaMailSender mailSender;
	
	@Value("${mail.fromAddr}")
	String from;
	
	public void sendRegistrationMail(String R_emial, String R_name) {
		try {
			MimeMessage mimeMessage = mailSender.createMimeMessage();
			MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, "utf-8");
			helper.setFrom(from);
			helper.setTo(R_emial);
			helper.setSubject("Welcome to Deep Dark Fantasy!!");
			String html = String.format("<p>Hi, %s sir,</p><p>Welcome to 514 Dungeon!!</p><p>Enjoy it!</p><p>Sent at %s</p>", R_name,
					LocalDateTime.now());
			helper.setText(html, true);
			mailSender.send(mimeMessage);
		} catch (MessagingException e) {
			throw new RuntimeException(e);
		}
	}
}
