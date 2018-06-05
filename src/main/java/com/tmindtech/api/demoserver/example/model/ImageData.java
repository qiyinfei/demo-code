package com.tmindtech.api.demoserver.example.model;

public class ImageData {
    public String msg;
    public String errorMsg;
    public String data;
    public Integer code;

    public ImageData() {
    }

    public ImageData(String msg, String errorMsg, String data, Integer code) {
        this.msg = msg;
        this.errorMsg = errorMsg;
        this.data = data;
        this.code = code;
    }
}
