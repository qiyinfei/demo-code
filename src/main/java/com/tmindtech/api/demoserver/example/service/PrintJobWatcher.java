package com.tmindtech.api.demoserver.example.service;

import javax.print.DocPrintJob;
import javax.print.event.PrintJobAdapter;
import javax.print.event.PrintJobEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PrintJobWatcher {
    private boolean done = false;
    Logger logger = LoggerFactory.getLogger(PrintJobWatcher.class);

    PrintJobWatcher(DocPrintJob job) {
        job.addPrintJobListener(new PrintJobAdapter() {
            public void printJobCanceled(PrintJobEvent pje) {
                synchronized (PrintJobWatcher.this) {
                    done = true;
                    PrintJobWatcher.this.notify();
                    logger.info("取消打印任务！");
                }
            }

            public void printJobCompleted(PrintJobEvent pje) {
                synchronized (PrintJobWatcher.this) {
                    done = true;
                    PrintJobWatcher.this.notify();
                    logger.info("打印任务完成！");
                }
            }

            public void printJobFailed(PrintJobEvent pje) {
                synchronized (PrintJobWatcher.this) {
                    done = true;
                    PrintJobWatcher.this.notify();
                    logger.info("打印任务失败！");
                }
            }

            public void printJobNoMoreEvents(PrintJobEvent pje) {
                synchronized (PrintJobWatcher.this) {
                    done = true;
                    PrintJobWatcher.this.notify();
                    logger.info("暂无打印任务！");
                }
            }
        });
    }

    public synchronized void waitForDone() {
        try {
            while (!done) {
                wait();
            }
        } catch (InterruptedException e) {
        }
    }
}
