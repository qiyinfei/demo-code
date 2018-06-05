package com.tmindtech.api.demoserver.example.model;

public class ExecutorDemo {
    public static void main(String[] args) {
        String str = "abcdefghijklmn";
        int index = str.indexOf("CDEFG");
        System.out.println(index);
        System.out.println(str.substring(index + "cdefg".length()));
    }
}
