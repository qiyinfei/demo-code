package com.tmindtech.api.demoserver.print;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import javax.imageio.ImageIO;
import javax.imageio.stream.ImageOutputStream;
import javax.print.Doc;
import javax.print.DocFlavor;
import javax.print.DocPrintJob;
import javax.print.PrintException;
import javax.print.PrintServiceLookup;
import javax.print.SimpleDoc;
import javax.print.attribute.HashPrintRequestAttributeSet;
import javax.print.attribute.standard.Copies;
import javax.print.attribute.standard.MediaSizeName;
import javax.print.attribute.standard.OrientationRequested;
import javax.print.attribute.standard.PrintQuality;

public class PrintLabelUtil {

    private static void printStream(InputStream inputStream, int index, int size) {
        InputStream stream;
        try {
            BufferedImage image = ImageIO.read(inputStream);
            BufferedImage subImage = image.getSubimage(0, (int) (index * 1.0 / size * image.getHeight()),
                    image.getWidth(), image.getHeight() / size);
            ByteArrayOutputStream bs = new ByteArrayOutputStream();
            ImageOutputStream ios = ImageIO.createImageOutputStream(bs);
            ImageIO.write(subImage, "png", ios);
            stream = new ByteArrayInputStream(bs.toByteArray());

            DocFlavor dof = DocFlavor.INPUT_STREAM.PNG;
            HashPrintRequestAttributeSet pras = new HashPrintRequestAttributeSet();
            pras.add(OrientationRequested.PORTRAIT);
            pras.add(PrintQuality.HIGH);
            pras.add(new Copies(1));
            pras.add(MediaSizeName.ISO_A6);
            try {
                Doc doc = new SimpleDoc(stream, dof, null);
                DocPrintJob job = PrintServiceLookup.lookupDefaultPrintService().createPrintJob();
                job.print(doc, pras);
            } catch (PrintException ex) {
                ex.printStackTrace();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private static void downloadImageStream(String imageUrl) {
        URL url;
        try {
            url = new URL(imageUrl);
            DataInputStream dataInputStream = new DataInputStream(url.openStream());

            ByteArrayOutputStream output = new ByteArrayOutputStream();

            byte[] buffer = new byte[1024];
            int length;

            while ((length = dataInputStream.read(buffer)) > 0) {
                output.write(buffer, 0, length);
            }
            dataInputStream.close();

            InputStream inputStream = new ByteArrayInputStream(output.toByteArray());
            Path path = Paths.get("c:/ems.png");
            Files.copy(inputStream, path);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public static void main(String[] args) {
        try {
            FileInputStream fileInputStream = new FileInputStream(new File("c:/ems.png"));

            InputStreamCacher cacher = new InputStreamCacher(fileInputStream);

            for (int i = 0; i < 3; i++) {
                printStream(cacher.getInputStream(), i, 3);
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
}
