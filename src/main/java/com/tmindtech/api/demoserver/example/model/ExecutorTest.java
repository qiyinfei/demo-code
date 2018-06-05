package com.tmindtech.api.demoserver.example.model;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ExecutorTest {

    public ExecutorService executorService = Executors.newFixedThreadPool(4);

    public void test1() {
        executorService.execute(() -> {
            System.out.println("test1开始...");
            try {
                Thread.sleep(9000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("test1结束...");
        });
    }

    public void test2() {
        executorService.execute(() -> {
            System.out.println("test2开始...");
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("test2结束...");
        });
    }

}
