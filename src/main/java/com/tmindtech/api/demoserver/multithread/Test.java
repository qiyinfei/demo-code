package com.tmindtech.api.demoserver.multithread;

import java.sql.Timestamp;
import java.time.Instant;

public class Test {

    public String serverAddress;

    public String appkey;

    public String secretkey;

    public void init(String appkey, String secretkey) {
        this.appkey = appkey;
        this.secretkey = secretkey;

        new Thread(() -> {
            while (true) {
                serverAddress = getServerAddress();

                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    break;
                }
            }
        }).start();
    }

    public String getServerAddress() {
        return "当前时间：" + Timestamp.from(Instant.now());
    }

    public static void main(String[] args) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 20000000; i++) {
            sb.append("我");
        }
        String result = sb.toString();
        System.out.println(result);
        System.out.println();
        System.out.println(result.length());
    }

}
