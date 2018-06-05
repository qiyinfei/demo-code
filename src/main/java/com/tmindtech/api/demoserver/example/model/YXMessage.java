package com.tmindtech.api.demoserver.example.model;

public class YXMessage {
    public String id;
    public Integer async;
    public String topic;
    public String source;
    public String target;
    public String timestamp;
    public String sign;
    public String payload;

    @Override
    public String toString() {
        return "YXMessage{" +
                "id='" + id + '\'' +
                ", async=" + async +
                ", topic='" + topic + '\'' +
                ", source='" + source + '\'' +
                ", target='" + target + '\'' +
                ", timestamp='" + timestamp + '\'' +
                ", sign='" + sign + '\'' +
                ", payload='" + payload + '\'' +
                '}';
    }
}
