package com.tmindtech.api.demoserver.example.model;

import java.util.Comparator;
import java.util.Optional;
import java.util.Set;
import javafx.print.PrintResolution;
import javafx.print.Printer;
import javax.print.DocFlavor;
import javax.print.PrintService;
import javax.print.PrintServiceLookup;
import javax.print.attribute.Attribute;
import javax.print.attribute.HashPrintRequestAttributeSet;
import javax.print.attribute.PrintRequestAttributeSet;
import javax.print.attribute.PrintServiceAttributeSet;
import javax.print.attribute.standard.PrinterResolution;

public class Cargo {
    public String skuId; // 商品skuId

    public Integer count; // 数量

    /**
     * 信息
     */
    public static void info() {
        PrintService printService = PrintServiceLookup.lookupDefaultPrintService();
        PrinterResolution[] resolutions = (PrinterResolution[]) printService.getSupportedAttributeValues(PrinterResolution.class, null, null);
        System.out.println(resolutions[0].getCrossFeedResolution(PrinterResolution.DPI));
        System.out.println(resolutions[0].getFeedResolution(PrinterResolution.DPI));

//        Set<PrintResolution> set = Printer.getDefaultPrinter().getPrinterAttributes().getSupportedPrintResolutions();
//        Optional<PrintResolution> optional = set.stream().max(Comparator.comparingInt(PrintResolution::getFeedResolution));
//        PrintResolution resolution = optional.orElseGet(null);
//        System.out.println(resolution.getCrossFeedResolution());
//        System.out.println(resolution.getFeedResolution());

        PrintRequestAttributeSet attrrrr = new HashPrintRequestAttributeSet();
        PrinterResolution resolution = new PrinterResolution(300, 300, PrinterResolution.DPI);
        attrrrr.add(resolution);
        PrintService[] pservices = PrintServiceLookup.lookupPrintServices(null, attrrrr);
        System.out.println("打印机个数： " + pservices.length);

        Set<PrintResolution> set = Printer.getDefaultPrinter().getPrinterAttributes().getSupportedPrintResolutions();
        Optional<PrintResolution> optional = set.stream().max(Comparator.comparingInt(PrintResolution::getFeedResolution));
        PrintResolution printResolution = optional.orElseGet(null);

        for (PrintService pservice : pservices) {
            System.out.println("打印机： " + pservice.getName());
            System.out.println("属性: ");
            PrintServiceAttributeSet psa = pservice.getAttributes();
            Attribute[] attrs = psa.toArray();
            for (Attribute attr : attrs) {
                System.out.println(attr.getName() + " : : : : : :" + attr.getCategory().getName());
            }
            System.out.println("文档类型: ");
            DocFlavor[] flavors = pservice.getSupportedDocFlavors();
            for (DocFlavor dFlavor : flavors) {
                System.out.print(dFlavor.getMimeType() + " : ");
                System.out.println(dFlavor.getRepresentationClassName());
            }
            System.out.println();
        }
    }

    public static void main(String[] args) {
        System.out.println((1 * 1.0 / 3 * 2));
    }

}
