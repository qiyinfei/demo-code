package com.tmindtech.api.demoserver.bankcheck;

public class ServerDemoTest {
    public static void main(String[] args) {
        ServerDemo serverDemo = new ServerDemo("http://47.100.64.170/");
        serverDemo.getServerStatus();
    }
}
