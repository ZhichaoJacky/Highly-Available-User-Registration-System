package com.itranswarp.learnjava;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;

import io.lettuce.core.RedisClient;
import io.lettuce.core.RedisURI;

@ConfigurationProperties("spring.redis")
public class RedisConfiguration {

	private String host;
	private int port;
	private String password;
	private int database;
	private String name;

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public int getDatabase() {
		return database;
	}

	public void setDatabase(int database) {
		this.database = database;
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	/*
	@Bean
	RedisClient redisClient() {
		RedisURI uri = RedisURI.Builder.redis(this.host, this.port).withPassword(this.password)
				.withDatabase(this.database).build();
		return RedisClient.create(uri);
	}
	*/
	//主从复制，哨兵实现 sentinel
	@Bean
	RedisClient redisClient() {
		RedisURI uri = RedisURI.Builder.sentinel(this.host, 26379, this.name).withSentinel(this.host, 26380).withSentinel(this.host, 26381)
				.build();
		return RedisClient.create(uri);
	}
	
}
