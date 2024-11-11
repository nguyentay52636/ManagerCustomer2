package com.example.baitapquatrinh2.Utils;


import java.io.FileOutputStream;
import java.io.IOException;

public class FileUtils {

    public static void saveToXmlFile(String xmlData, String filePath) {
        try (FileOutputStream fos = new FileOutputStream(filePath)) {
            fos.write(xmlData.getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
