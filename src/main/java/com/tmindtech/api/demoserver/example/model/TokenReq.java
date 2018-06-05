package com.tmindtech.api.demoserver.example.model;

public class TokenReq {
    public String appid;
    public Long timestamp;
    public String sign;
    public String phone;
    public String name;
    public String idcard;

    public TokenReq(String appid, Long timestamp, String sign, String phone, String name, String idcard) {
        this.appid = appid;
        this.timestamp = timestamp;
        this.sign = sign;
        this.phone = phone;
        this.name = name;
        this.idcard = idcard;
    }
}
