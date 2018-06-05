package com.tmindtech.api.demoserver.example.service;

import com.tmindtech.api.demoserver.example.model.PrinterMsg;
import java.io.IOException;
import java.util.Arrays;
import javax.annotation.PostConstruct;
import javax.print.Doc;
import javax.print.DocFlavor;
import javax.print.DocPrintJob;
import javax.print.PrintException;
import javax.print.PrintService;
import javax.print.PrintServiceLookup;
import javax.print.SimpleDoc;
import javax.print.attribute.DocAttributeSet;
import javax.print.attribute.HashAttributeSet;
import javax.print.attribute.HashDocAttributeSet;
import javax.print.attribute.HashPrintRequestAttributeSet;
import javax.print.attribute.PrintRequestAttributeSet;
import javax.print.attribute.standard.Copies;
import javax.print.attribute.standard.MediaPrintableArea;
import javax.print.attribute.standard.OrientationRequested;
import javax.print.attribute.standard.PrintQuality;
import javax.print.attribute.standard.PrinterName;
import javax.print.event.PrintJobAdapter;
import javax.print.event.PrintJobEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class PrinterService {
    private Logger logger = LoggerFactory.getLogger(PrinterService.class);

    private PrintService printService;

    @Value("${printer_name}")
    String printerName;

    @Value("${printer_width}")
    Integer printerWidth;

    @Value("${printer_height}")
    Integer printerHeight;

    @Value("${printer_xoffset}")
    Integer printerXoffset;

    @Value("${printer_yoffset}")
    Integer printerYoffset;

    @PostConstruct
    public void initPrintService() {
        DocFlavor dof = DocFlavor.INPUT_STREAM.JPEG;
        HashAttributeSet hs = new HashAttributeSet();
        hs.add(new PrinterName(printerName, null));
        PrintService[] printServices = PrintServiceLookup.lookupPrintServices(dof, hs);
        if (printServices.length == 0) {
            logger.info("打印机初始化失败！！！");
            return;
        }
        logger.info("打印机初始化成功！");
        logger.info("获取的打印机列表如下：");
        Arrays.asList(printServices).forEach(item -> logger.info(item.getName()));
        printService = printServices[0];
    }

    public PrinterMsg printOrderTask(MultipartFile file, int count) {
        PrinterMsg msg = new PrinterMsg();
        DocFlavor dof = DocFlavor.INPUT_STREAM.JPEG;
        PrintRequestAttributeSet pras = new HashPrintRequestAttributeSet();
        pras.add(OrientationRequested.PORTRAIT);
        pras.add(PrintQuality.HIGH);
        pras.add(new Copies(count));
        DocAttributeSet das = new HashDocAttributeSet();
        das.add(new MediaPrintableArea(printerXoffset, printerYoffset, printerWidth, printerHeight, MediaPrintableArea.MM));
        try {
            Doc doc = new SimpleDoc(file.getInputStream(), dof, das);
            DocPrintJob job = printService.createPrintJob();
            job.addPrintJobListener(new PrintJobAdapter() {
                @Override
                public void printDataTransferCompleted(PrintJobEvent pje) {
                    synchronized (PrinterService.class) {
                        msg.code = 0;
                        msg.result = "数据传输成功";
                        logger.info("数据传输成功！");
                    }
                }

                @Override
                public void printJobCompleted(PrintJobEvent pje) {
                    synchronized (PrinterService.class) {
                        msg.code = 1;
                        msg.result = "打印服务成功";
                        logger.info("打印服务成功！");
                    }
                }

                @Override
                public void printJobFailed(PrintJobEvent pje) {
                    synchronized (PrinterService.class) {
                        msg.code = 2;
                        msg.result = "打印服务失败";
                        logger.info("打印服务失败！");
                    }
                }

                @Override
                public void printJobCanceled(PrintJobEvent pje) {
                    synchronized (PrinterService.class) {
                        msg.code = 3;
                        msg.result = "打印服务已取消";
                        logger.info("打印服务已取消！");
                    }
                }

                @Override
                public void printJobNoMoreEvents(PrintJobEvent pje) {
                    synchronized (PrinterService.class) {
                        msg.code = 4;
                        msg.result = "打印服务全部完成，等待其他打印服务";
                        logger.info("打印服务全部完成，等待其他打印服务！");
                    }
                }

                @Override
                public void printJobRequiresAttention(PrintJobEvent pje) {
                    synchronized (PrinterService.class) {
                        msg.code = 5;
                        msg.result = "可以恢复的打印服务，如打印机缺纸";
                        logger.info("可以恢复的打印服务，如打印机缺纸！");
                    }
                }
            });
            job.print(doc, pras);
            return msg;
        } catch (IOException | PrintException ex) {
            logger.info("打印故障，具体原因为：" + ex.getMessage());
            return null;
        }
    }

}
