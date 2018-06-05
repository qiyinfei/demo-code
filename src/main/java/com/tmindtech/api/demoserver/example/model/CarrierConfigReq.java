package com.tmindtech.api.demoserver.example.model;

public class CarrierConfigReq {
    public String appid;
    public Long timestamp;
    public String sign;
    public String phone;
    public String token;

    public CarrierConfigReq(String appid, Long timestamp, String sign, String phone, String token) {
        this.appid = appid;
        this.timestamp = timestamp;
        this.sign = sign;
        this.phone = phone;
        this.token = token;
    }
}
