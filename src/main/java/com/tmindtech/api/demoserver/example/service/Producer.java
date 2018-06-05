package com.tmindtech.api.demoserver.example.service;

import javax.jms.Queue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsMessagingTemplate;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.stereotype.Service;

@Service
@EnableScheduling
public class Producer {

    @Autowired
    private Queue queue;

    @Autowired
    JmsMessagingTemplate jmsMessagingTemplate;

    public void sendMessage(String message) {
        this.jmsMessagingTemplate.convertAndSend(queue, message);
        System.out.println("一条消息进入了消息队列，内容为： " + message);
    }

}
