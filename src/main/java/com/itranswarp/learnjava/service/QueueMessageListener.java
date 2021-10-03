package com.itranswarp.learnjava.service;

import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.ModelAndView;

import com.itranswarp.learnjava.entity.User;
import com.itranswarp.learnjava.messaging.LoginMessage;
import com.itranswarp.learnjava.messaging.RegistrationMessage;

@Component
public class QueueMessageListener {
	
	@Autowired
	MailService mailService;
	
	private final Logger logger = LoggerFactory.getLogger(getClass());

	//static final String QUEUE_MAIL = "q_mail";
	//static final String QUEUE_SMS = "q_sms";
	//static final String QUEUE_APP = "q_app";
	static final String QUEUE_SEND_MAIL = "q_newUsers";
	
	//维护一个线程池，用于处理异步邮件发送
	private ThreadPoolExecutor executor = new ThreadPoolExecutor(5, 10, 200, TimeUnit.MILLISECONDS, 
			new ArrayBlockingQueue<Runnable>(5));
	
	@RabbitListener(queues = QUEUE_SEND_MAIL)
	public void onRegistrationMessageFromMailQueue(RegistrationMessage message) throws Exception {
		logger.info("queue {} received registration message: {}", QUEUE_SEND_MAIL, message);
		System.out.println(message.email);
		/*
		try {
			// send registration mail(开启一个新线程处理，可使用多线程):
			new Thread(() -> {
				mailService.sendRegistrationMail(message.email, message.name);
			}).start();
		} catch (RuntimeException e) {
			return;
		}
		*/
		executor.execute(new Runnable() {
			
			public void run() {
				mailService.sendRegistrationMail(message.email, message.name);
			}
		});
	}
	
	/*
	@RabbitListener(queues = QUEUE_MAIL)
	public void onRegistrationMessageFromMailQueue(RegistrationMessage message) throws Exception {
		logger.info("queue {} received registration message: {}", QUEUE_MAIL, message);
	}

	@RabbitListener(queues = QUEUE_SMS)
	public void onRegistrationMessageFromSmsQueue(RegistrationMessage message) throws Exception {
		logger.info("queue {} received registration message: {}", QUEUE_SMS, message);
	}

	@RabbitListener(queues = QUEUE_MAIL)
	public void onLoginMessageFromMailQueue(LoginMessage message) throws Exception {
		logger.info("queue {} received message: {}", QUEUE_MAIL, message);
	}

	@RabbitListener(queues = QUEUE_SMS)
	public void onLoginMessageFromSmsQueue(LoginMessage message) throws Exception {
		logger.info("queue {} received message: {}", QUEUE_SMS, message);
	}

	@RabbitListener(queues = QUEUE_APP)
	public void onLoginMessageFromAppQueue(LoginMessage message) throws Exception {
		logger.info("queue {} received message: {}", QUEUE_APP, message);
	}
	*/
}