package com.example.baitapquatrinh2.Services;

import android.content.ContentValues;
import android.content.Context;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;

import org.w3c.dom.*;
import javax.xml.parsers.*;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import com.example.baitapquatrinh2.Models.Customer;

public class XMLHelper {
public static File exportCustomersToXML(List<Customer> customers, Context context) {
    try {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document document = builder.newDocument();

        // Tạo phần tử gốc <customers>
        Element root = document.createElement("customers");
        document.appendChild(root);

        // Thêm các đối tượng Customer vào XML
        for (Customer customer : customers) {
            Element customerElement = document.createElement("customer");

            Element phoneNumber = document.createElement("phoneNumber");
            phoneNumber.appendChild(document.createTextNode(customer.getPhoneNumber()));
            customerElement.appendChild(phoneNumber);

            Element currentPoint = document.createElement("currentPoint");
            currentPoint.appendChild(document.createTextNode(String.valueOf(customer.getCurrentPoint())));
            customerElement.appendChild(currentPoint);

            Element creationDate = document.createElement("creationDate");
            creationDate.appendChild(document.createTextNode(customer.getCreationDate()));
            customerElement.appendChild(creationDate);

            Element lastUpdatedDate = document.createElement("lastUpdatedDate");
            lastUpdatedDate.appendChild(document.createTextNode(customer.getLastUpdatedDate()));
            customerElement.appendChild(lastUpdatedDate);

            Element note = document.createElement("note");
            note.appendChild(document.createTextNode(customer.getNote()));
            customerElement.appendChild(note);

            root.appendChild(customerElement);
        }

        // Sử dụng MediaStore API để lưu file vào thư mục Downloads
        ContentValues values = new ContentValues();
        values.put(MediaStore.MediaColumns.DISPLAY_NAME, "customers.xml");
        values.put(MediaStore.MediaColumns.MIME_TYPE, "text/xml");
        values.put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_DOWNLOADS);

        Uri uri = context.getContentResolver().insert(MediaStore.Files.getContentUri("external"), values);

        if (uri != null) {
            try (OutputStream outputStream = context.getContentResolver().openOutputStream(uri)) {
                if (outputStream != null) {
                    TransformerFactory transformerFactory = TransformerFactory.newInstance();
                    Transformer transformer = transformerFactory.newTransformer();
                    transformer.setOutputProperty(javax.xml.transform.OutputKeys.INDENT, "yes");
                    transformer.transform(new DOMSource(document), new StreamResult(outputStream));

                    Log.d("ExportXML", "File saved successfully at: " + uri.toString());
                    return new File(uri.getPath()); // Trả về file đã lưu
                }
            }
        }
    } catch (Exception e) {
        e.printStackTrace();
    }
    return null;
}

    public static List<Customer> importCustomersFromXML(Context context, Uri fileUri) {
        List<Customer> customerList = new ArrayList<>();

        try (InputStream inputStream = context.getContentResolver().openInputStream(fileUri)) {
            if (inputStream == null) {
                Log.e("ImportXML", "InputStream null: Không thể mở file từ Uri");
                return customerList;
            }

            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document = builder.parse(inputStream);

            // Lấy danh sách các phần tử <customer>
            NodeList nodeList = document.getElementsByTagName("customer");
            if (nodeList.getLength() == 0) {
                Log.e("ImportXML", "File không chứa thẻ <customer>");
                return customerList;
            }

            for (int i = 0; i < nodeList.getLength(); i++) {
                Node node = nodeList.item(i);
                if (node.getNodeType() == Node.ELEMENT_NODE) {
                    Element element = (Element) node;

                    // Lấy dữ liệu từ các thẻ con
                    String phoneNumber = element.getElementsByTagName("phoneNumber").item(0).getTextContent();
                    int currentPoint = Integer.parseInt(element.getElementsByTagName("currentPoint").item(0).getTextContent());
                    String creationDate = element.getElementsByTagName("creationDate").item(0).getTextContent();
                    String lastUpdatedDate = element.getElementsByTagName("lastUpdatedDate").item(0).getTextContent();
                    String note = element.getElementsByTagName("note").item(0).getTextContent();

                    // Tạo đối tượng Customer và thêm vào danh sách
                    Customer customer = new Customer(phoneNumber, currentPoint, creationDate, lastUpdatedDate, note);
                    customerList.add(customer);
                }
            }

        } catch (FileNotFoundException e) {
            Log.e("ImportXML", "File không tìm thấy: " + e.getMessage());
        } catch (NumberFormatException e) {
            Log.e("ImportXML", "Lỗi chuyển đổi dữ liệu số: " + e.getMessage());
        } catch (Exception e) {
            Log.e("ImportXML", "Lỗi khi đọc file: " + e.getMessage());
        }

        return customerList;
    }


}
