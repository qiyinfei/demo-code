package com.tmindtech.api.demoserver.example.service;

import com.tmindtech.api.demoserver.example.db.UserMapper;
import com.tmindtech.api.demoserver.example.model.User;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import javax.print.DocFlavor;
import javax.print.PrintService;
import javax.print.PrintServiceLookup;
import javax.print.ServiceUI;
import javax.print.attribute.HashPrintRequestAttributeSet;
import javax.print.attribute.PrintRequestAttributeSet;
import javax.print.attribute.standard.Copies;
import javax.print.attribute.standard.OrientationRequested;
import javax.print.attribute.standard.PrintQuality;
import javax.print.attribute.standard.PrinterName;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class ExampleService {
    Logger logger = LoggerFactory.getLogger(ExampleService.class);

    @Autowired
    UserMapper userMapper;

    public User addUser(User user) {
        userMapper.insertSelective(user);
        return user;
    }

    public List<User> getUserByName(String name) {
        return userMapper.getUserByName(name);
    }

    public User getUserDetail(long id) {
        User user = userMapper.selectByPrimaryKey(id);
        if (user == null) {
            try {
                throw new Exception();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return user;
    }

    public void updateUser(User user, long id) {
        User exist = userMapper.selectByPrimaryKey(id);
        if (exist == null) {
            try {
                throw new Exception();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        user.id = id;
        userMapper.updateByPrimaryKeySelective(user);
    }

    public void deleteUser(long id) {
        User user = userMapper.selectByPrimaryKey(id);
        if (user == null) {
            try {
                throw new Exception();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        userMapper.deleteByPrimaryKey(user);
    }

    public void printPicture(MultipartFile file, int count) {

        DocFlavor dof = DocFlavor.INPUT_STREAM.JPEG;
        PrintRequestAttributeSet pras = new HashPrintRequestAttributeSet();
        pras.add(OrientationRequested.PORTRAIT);
        pras.add(new Copies(count));
        pras.add(PrintQuality.HIGH);
        PrintService[] printServices = PrintServiceLookup.lookupPrintServices(dof, pras);
        PrintService defaultPs = PrintServiceLookup.lookupDefaultPrintService();
        PrintService ps = ServiceUI.printDialog(null, 200, 200, printServices, defaultPs, dof, pras);
        logger.info("默认打印机：");
        logger.info(defaultPs.getName());
        logger.info("默认打印机！");
        logger.info("所有打印机：");
        Arrays.asList(printServices).forEach(i -> logger.info(i.getName()));
        logger.info("所有打印机！");
        logger.info("最终打印机：");
        logger.info(ps == null ? "null" : ps.getName());
        logger.info("最终打印机！");


//        DocFlavor dof = DocFlavor.INPUT_STREAM.JPEG;
//        HashAttributeSet hs = new HashAttributeSet();
//        hs.add(new PrinterName(printerName, null));
//        PrintService[] ps = PrintServiceLookup.lookupPrintServices(dof, null);
//        PrintRequestAttributeSet pras = new HashPrintRequestAttributeSet();
//        pras.add(OrientationRequested.PORTRAIT);
//        pras.add(new Copies(count));
//        pras.add(PrintQuality.HIGH);
//        DocAttributeSet das = new HashDocAttributeSet();
//        das.add(new MediaPrintableArea(0, 0, 210, 296, MediaPrintableArea.MM));
//        logger.info("默认的打印机名称如下：");
//        logger.info(PrintServiceLookup.lookupDefaultPrintService().getName());
//        logger.info("以上就是默认的打印机名称!");
//        logger.info("所有打印机名称如下：");
//        Arrays.asList(ps).forEach(item -> logger.info(item.getName()));
//        logger.info("以上就是所有查询到的打印机！");
//        logger.info(ps[0].getName());
//        try {
//            Doc doc = new SimpleDoc(file.getInputStream(), dof, das);
//            DocPrintJob job = ps.createPrintJob();
//            job.print(doc, pras);
//        } catch (IOException | PrintException ex) {
//            ex.printStackTrace();
//        }
    }

}
