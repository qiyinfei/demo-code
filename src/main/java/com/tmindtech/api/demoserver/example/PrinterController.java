package com.tmindtech.api.demoserver.example;

import com.tmindtech.api.demoserver.base.annotation.AwesomeParam;
import com.tmindtech.api.demoserver.example.model.SaleOrder;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PrinterController {

    @GetMapping("api/v1/sale_order")
    public List<String> getUniqueCodeListByOrder(@AwesomeParam String saleOrder) {
        List<String> list = new ArrayList<>();
        list.add("300dpi.png");
        return list;
    }

    @PostMapping("api/v1/split")
    public List<String> getUniqueCodeListBySplitOrder(@RequestBody SaleOrder saleOrder) {
        List<String> list = new ArrayList<>();
        list.add("300dpi.png");
        return list;
    }


    public static void main(String[] args) throws Exception {
        byte[] bytes = File2byte("C:\\code\\waybill\\waybill-sdk\\miandan.jpg");
        byte[] newB = resizePixels(bytes, 800, 1200, 1181, 1772);
        byte2File(newB, "C:\\code\\waybill\\waybill-sdk", "3.jpg");
    }

    public static byte[] File2byte(String filePath) {
        byte[] buffer = null;
        try {
            File file = new File(filePath);
            FileInputStream fis = new FileInputStream(file);
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            byte[] b = new byte[1024];
            int n;
            while ((n = fis.read(b)) != -1) {
                bos.write(b, 0, n);
            }
            fis.close();
            bos.close();
            buffer = bos.toByteArray();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return buffer;
    }

    public static byte[] resizePixels(byte[] pixels, int w1, int h1, int w2, int h2) {
        byte[] temp = new byte[w2 * h2];
        // EDIT: added +1 to account for an early rounding problem
        byte x_ratio = (byte) (((w1 << 16) / w2) + 1);
        byte y_ratio = (byte) (((h1 << 16) / h2) + 1);
        //int x_ratio = (int)((w1<<16)/w2) ;
        //int y_ratio = (int)((h1<<16)/h2) ;
        byte x2, y2;
        for (int i = 0; i < h2; i++) {
            for (int j = 0; j < w2; j++) {
                x2 = (byte) ((j * x_ratio) >> 16);
                y2 = (byte) ((i * y_ratio) >> 16);
                temp[(i * w2) + j] = pixels[(y2 * w1) + x2];
            }
        }
        return temp;
    }

    public static void byte2File(byte[] buf, String filePath, String fileName) {
        BufferedOutputStream bos = null;
        FileOutputStream fos = null;
        File file = null;
        try {
            File dir = new File(filePath);
            if (!dir.exists() && dir.isDirectory()) {
                dir.mkdirs();
            }
            file = new File(filePath + File.separator + fileName);
            fos = new FileOutputStream(file);
            bos = new BufferedOutputStream(fos);
            bos.write(buf);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (bos != null) {
                try {
                    bos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
