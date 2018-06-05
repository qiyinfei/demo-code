package com.tmindtech.api.demoserver.example.service;

import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Service;

@Service
public class Customer {

    @JmsListener(destination = "demo.queue")
    public String receiveMessage(String text) {
        System.out.println("从消息队列中读到一条新消息，新消息内容为： " + text);
        return text;
    }
}
