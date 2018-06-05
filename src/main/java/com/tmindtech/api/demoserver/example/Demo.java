package com.tmindtech.api.demoserver.example;

import com.alibaba.fastjson.JSONObject;
import com.tmindtech.api.demoserver.example.model.Message;
import java.util.UUID;

public class Demo {
    public static void main(String[] args) {
        Message message = new Message(UUID.randomUUID().toString(),"qq", 22, "java");
        System.out.println(JSONObject.toJSONString(message));
    }
}
