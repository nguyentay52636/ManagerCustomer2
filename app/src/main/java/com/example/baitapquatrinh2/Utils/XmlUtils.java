package com.example.baitapquatrinh2.Utils;

import com.example.baitapquatrinh2.Models.Customer;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import java.io.StringWriter;
import java.util.List;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import java.util.List;

public class XmlUtils {

    public static String convertToXml(List<Customer> customers) {
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.newDocument();

            Element rootElement = doc.createElement("customers");
            doc.appendChild(rootElement);

            for (Customer customer : customers) {
                Element customerElement = doc.createElement("customer");

                Element phoneNumber = doc.createElement("phoneNumber");
                phoneNumber.appendChild(doc.createTextNode(customer.getPhoneNumber()));
                customerElement.appendChild(phoneNumber);

                Element currentPoint = doc.createElement("currentPoint");
                currentPoint.appendChild(doc.createTextNode(String.valueOf(customer.getCurrentPoint())));
                customerElement.appendChild(currentPoint);

                Element creationDate = doc.createElement("creationDate");
                creationDate.appendChild(doc.createTextNode(customer.getCreationDate()));
                customerElement.appendChild(creationDate);

                Element lastUpdatedDate = doc.createElement("lastUpdatedDate");
                lastUpdatedDate.appendChild(doc.createTextNode(customer.getLastUpdatedDate()));
                customerElement.appendChild(lastUpdatedDate);

                Element note = doc.createElement("note");
                note.appendChild(doc.createTextNode(customer.getNote()));
                customerElement.appendChild(note);

                rootElement.appendChild(customerElement);
            }

            return documentToString(doc);
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    private static String documentToString(Document doc) {
        try {
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            StringWriter writer = new StringWriter();
            transformer.transform(new DOMSource(doc), new StreamResult(writer));
            return writer.getBuffer().toString();
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }
}
