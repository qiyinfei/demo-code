package com.tmindtech.api.demoserver.example.model;

public class AuthInfoReq {
    public String appid;
    public Long timestamp;
    public String sign;
    public String token;

    public AuthInfoReq(String appid, Long timestamp, String sign, String token) {
        this.appid = appid;
        this.timestamp = timestamp;
        this.sign = sign;
        this.token = token;
    }
}
