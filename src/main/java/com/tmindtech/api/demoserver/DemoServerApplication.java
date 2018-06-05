package com.tmindtech.api.demoserver;

import javax.jms.Queue;
import javax.jms.Topic;
import org.apache.activemq.command.ActiveMQQueue;
import org.apache.activemq.command.ActiveMQTopic;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class DemoServerApplication {

	@Bean
	public Queue jmsQueue() {
		return new ActiveMQQueue("demo.queue");
	}

	public static void main(String[] args) {
		SpringApplication.run(DemoServerApplication.class, args);
	}
}
