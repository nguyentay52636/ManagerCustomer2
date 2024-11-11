package com.example.baitapquatrinh2.Utils;

import android.os.Environment;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;

public class ExportXmlFile {

    public static void exportXml(String fileName, String rootElementName, String[][] elements) {
        try {
            // Tạo một document XML mới
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.newDocument();

            // Tạo phần tử gốc
            Element rootElement = doc.createElement(rootElementName);
            doc.appendChild(rootElement);

            // Thêm các phần tử con từ mảng `elements`
            for (String[] elementData : elements) {
                Element itemElement = doc.createElement("Item");

                Element name = doc.createElement("Name");
                name.appendChild(doc.createTextNode(elementData[0]));
                itemElement.appendChild(name);

                Element phone = doc.createElement("Phone");
                phone.appendChild(doc.createTextNode(elementData[1]));
                itemElement.appendChild(phone);

                rootElement.appendChild(itemElement);
            }

            // Lưu XML vào bộ nhớ ngoài
            File file = new File(Environment.getExternalStorageDirectory(), fileName + ".xml");
            OutputStream outputStream = new FileOutputStream(file);

            // Ghi dữ liệu vào file XML
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            DOMSource domSource = new DOMSource(doc);
            StreamResult streamResult = new StreamResult(outputStream);
            transformer.transform(domSource, streamResult);

            outputStream.close();
            System.out.println("XML file created successfully at: " + file.getAbsolutePath());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
