package com.itranswarp.learnjava;

import java.util.Properties;

import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Scope;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@SpringBootApplication
@Import(RedisConfiguration.class)
public class Application {

	public static void main(String[] args) throws Exception {
		SpringApplication.run(Application.class, args);
	}
	
	// -- RabbitMQ message converter ------------------------------------------

	@Bean
	MessageConverter createMessageConverter() {
		return new Jackson2JsonMessageConverter();
	}

	// -- javamail configuration ----------------------------------------------

	@Bean
	JavaMailSender createJavaMailSender(
			// properties:
			@Value("${spring.mail.host}") String host, @Value("${spring.mail.port}") int port, @Value("${spring.mail.auth}") String auth,
			@Value("${spring.mail.username}") String username, @Value("${spring.mail.password}") String password,
			@Value("${spring.mail.debug:true}") String debug) {
		var mailSender = new JavaMailSenderImpl();
		mailSender.setHost(host);
		mailSender.setPort(port);

		mailSender.setUsername(username);
		mailSender.setPassword(password);

		Properties props = mailSender.getJavaMailProperties();
		props.put("mail.transport.protocol", "smtp");
		props.put("mail.smtp.auth", auth);
		if (port == 587) {
			props.put("mail.smtp.starttls.enable", "true");
		}
		if (port == 465) {
			props.put("mail.smtp.socketFactory.port", "465");
			props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
		}
		props.put("mail.debug", debug);
		return mailSender;
	}


	// -- Mvc configuration ---------------------------------------------------

	@Bean
	WebMvcConfigurer createWebMvcConfigurer(@Autowired HandlerInterceptor[] interceptors) {
		return new WebMvcConfigurer() {
			@Override
			public void addResourceHandlers(ResourceHandlerRegistry registry) {
				registry.addResourceHandler("/static/**").addResourceLocations("classpath:/static/");
			}
		};
	}
}
