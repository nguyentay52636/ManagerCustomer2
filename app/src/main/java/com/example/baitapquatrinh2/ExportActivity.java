package com.example.baitapquatrinh2;
import androidx.annotation.Nullable;
import androidx.documentfile.provider.DocumentFile;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import android.content.ActivityNotFoundException;
import android.content.ContentUris;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import com.example.baitapquatrinh2.ContentProvider.CustomerProvider;
import com.example.baitapquatrinh2.Models.Customer;
import com.example.baitapquatrinh2.Utils.XMLHelper;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class ExportActivity extends AppCompatActivity {

    private Button btnExportFile, btnOpenFile, btnSendEmail;
    private TextView tvStatus;
    private File xmlFile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.filepaths);

        btnExportFile = findViewById(R.id.btnExportFile);
        btnOpenFile = findViewById(R.id.btnOpenFile);
        btnSendEmail = findViewById(R.id.btnSendEmail);
        tvStatus = findViewById(R.id.tvStatus);


        btnExportFile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<Customer> customers = CustomerProvider.loadCustomers(getApplicationContext());
                Log.d("CustomerListSize", "Customer list size: " + (customers != null ? customers.size() : 0));

                // Kiểm tra xem danh sách khách hàng có rỗng không
                if (customers == null || customers.isEmpty()) {
                    Log.e("ExportXML", "Danh sách khách hàng trống.");
                    tvStatus.setText("Danh sách khách hàng trống.");
                    Toast.makeText(ExportActivity.this, "Danh sách khách hàng trống!", Toast.LENGTH_SHORT).show();
                    return;
                }

//                String xmlContent = convertCustomersToXMLString(customers);
                // Log nội dung XML
//                Log.d("CustomerXMLContent", "XML content: \n" + xmlContent);

                File xmlFile = XMLHelper.exportCustomersToXML(customers, ExportActivity.this);

                if (xmlFile != null) {
                    tvStatus.setText("Xuất file thành công tại thư mục Downloads!");
                    Toast.makeText(ExportActivity.this, "Xuất file XML thành công!", Toast.LENGTH_SHORT).show();
                } else {
                    tvStatus.setText("Xuất file thất bại.");
                    Toast.makeText(ExportActivity.this, "Xuất file XML thất bại!", Toast.LENGTH_SHORT).show();
                }
            }
        });


        btnOpenFile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                openXmlFile();
                openXmlFileUsingDocumentFile();
            }
        });
        // Xử lý sự kiện gửi email
        btnSendEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                File xmlFile = new File("/storage/emulated/0/Android/data/com.example.baitapquatrinh2/files/Documents/customers.xml");

                // Đảm bảo file tồn tại và đường dẫn chính xác
                if (xmlFile != null && xmlFile.exists()) {
                    sendEmailWithAttachment(xmlFile); // Gọi phương thức gửi email với file đính kèm
                } else {
                    // Thông báo nếu file không tồn tại hoặc không hợp lệ
                    Toast.makeText(ExportActivity.this, "File không tồn tại để gửi email!", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
    private String convertCustomersToXMLString(List<Customer> customers) {
        StringBuilder xmlBuilder = new StringBuilder();
        xmlBuilder.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
        xmlBuilder.append("<customers>\n");

        for (Customer customer : customers) {
            xmlBuilder.append("    <customer>\n");
            xmlBuilder.append("        <phoneNumber>").append(customer.getPhoneNumber()).append("</phoneNumber>\n");
            xmlBuilder.append("        <currentPoint>").append(customer.getCurrentPoint()).append("</currentPoint>\n");
            xmlBuilder.append("        <creationDate>").append(customer.getCreationDate()).append("</creationDate>\n");
            xmlBuilder.append("        <lastUpdatedDate>").append(customer.getLastUpdatedDate()).append("</lastUpdatedDate>\n");
            xmlBuilder.append("        <note>").append(customer.getNote()).append("</note>\n");
            xmlBuilder.append("    </customer>\n");
        }

        xmlBuilder.append("</customers>");
        return xmlBuilder.toString();
    }

//    public void openXmlFile() {
//        try {
//            // Tên file cần tìm
//            String fileName = "customers.xml";
//
//            // Truy vấn MediaStore để lấy URI của file từ thư mục Downloads
//            Uri fileUri = null;
//            String selection = MediaStore.Files.FileColumns.DISPLAY_NAME + "=?";
//            String[] selectionArgs = new String[]{fileName};
//
//            // Truy vấn tệp từ MediaStore
//            try (Cursor cursor = getContentResolver().query(
//                    MediaStore.Files.getContentUri("external"),
//                    null, // Trả về tất cả các cột
//                    selection,
//                    selectionArgs,
//                    null // Không cần sắp xếp
//            )) {
//                if (cursor != null && cursor.moveToFirst()) {
//                    // Lấy ID của file trong MediaStore
//                    int idColumn = cursor.getColumnIndex(MediaStore.Files.FileColumns._ID);
//                    if (idColumn != -1) {
//                        long id = cursor.getLong(idColumn);
//
//                        // Tạo URI của file từ ID
//                        fileUri = ContentUris.withAppendedId(MediaStore.Files.getContentUri("external"), id);
//                        Log.d("FileUri", "URI của file: " + fileUri.toString());
//                    } else {
//                        Log.e("FileError", "Không tìm thấy cột ID");
//                    }
//                }
//            }
//
//            // Kiểm tra fileUri
//            if (fileUri != null) {
//                // Tạo Intent để mở file
//                Intent intent = new Intent(Intent.ACTION_VIEW);
//                intent.setDataAndType(fileUri, "text/xml");
//                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
//
//                // Kiểm tra ứng dụng có thể xử lý Intent không
//                if (intent.resolveActivity(getPackageManager()) != null) {
//                    startActivity(Intent.createChooser(intent, "Mở file XML với:"));
//                } else {
//                    Toast.makeText(this, "Không có ứng dụng nào hỗ trợ mở file XML!", Toast.LENGTH_SHORT).show();
//                    Log.e("IntentError", "Không tìm thấy ứng dụng hỗ trợ mở file XML");
//                }
//            } else {
//                Toast.makeText(this, "File không tồn tại trong thư mục Downloads!", Toast.LENGTH_SHORT).show();
//                Log.e("FileError", "Không tìm thấy file với tên: " + fileName);
//            }
//        } catch (Exception e) {
//            Log.e("OpenFileError", "Lỗi khi mở file: " + e.getMessage());
//            Toast.makeText(this, "Đã xảy ra lỗi khi mở file", Toast.LENGTH_SHORT).show();
//        }
//    }




    public void openXmlFile() {
        try {
            // Tên file cần tìm
            String fileName = "customers.xml";

            // Truy vấn MediaStore để lấy URI của file từ thư mục Downloads
            Uri fileUri = null;
            String selection = MediaStore.Files.FileColumns.DISPLAY_NAME + "=?";
            String[] selectionArgs = new String[]{fileName};

            // Truy vấn tệp từ MediaStore
            try (Cursor cursor = getContentResolver().query(
                    MediaStore.Files.getContentUri("external"),
                    null, // Trả về tất cả các cột
                    selection,
                    selectionArgs,
                    null // Không cần sắp xếp
            )) {
                if (cursor != null && cursor.moveToFirst()) {
                    // Lấy ID của file trong MediaStore
                    int idColumn = cursor.getColumnIndex(MediaStore.Files.FileColumns._ID);
                    if (idColumn != -1) {
                        long id = cursor.getLong(idColumn);

                        // Tạo URI của file từ ID
                        fileUri = ContentUris.withAppendedId(MediaStore.Files.getContentUri("external"), id);
                        Log.d("FileUri", "URI của file: " + fileUri.toString());
                    } else {
                        Log.e("FileError", "Không tìm thấy cột ID");
                    }
                }
            }

            // Kiểm tra fileUri
            if (fileUri != null) {
                // Tạo DocumentFile từ URI
                DocumentFile documentFile = DocumentFile.fromSingleUri(this, fileUri);

                if (documentFile != null && documentFile.exists()) {
                    InputStream inputStream = getContentResolver().openInputStream(fileUri);

                    // Sử dụng XmlPullParser để xử lý nội dung XML
                    XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
                    XmlPullParser parser = factory.newPullParser();
                    if (inputStream != null) {
                        parser.setInput(inputStream, null);

                        int eventType = parser.getEventType();
                        while (eventType != XmlPullParser.END_DOCUMENT) {
                            switch (eventType) {
                                case XmlPullParser.START_TAG:
                                    String tagName = parser.getName();
                                    if ("customer".equals(tagName)) {
                                        // Xử lý dữ liệu của khách hàng ở đây
                                        String customerId = parser.getAttributeValue(null, "id");
                                        String customerName = parser.nextText();
                                        Log.d("CustomerInfo", "ID: " + customerId + " Name: " + customerName);
                                    }
                                    break;
                            }
                            eventType = parser.next();
                        }
                        inputStream.close();
                    }
                }
            } else {
                Toast.makeText(this, "File không tồn tại trong thư mục Downloads!", Toast.LENGTH_SHORT).show();
                Log.e("FileError", "Không tìm thấy file với tên: " + fileName);
            }
        } catch (Exception e) {
            Log.e("OpenFileError", "Lỗi khi mở file: " + e.getMessage());
            Toast.makeText(this, "Đã xảy ra lỗi khi mở file", Toast.LENGTH_SHORT).show();
        }
    }




    public void openXmlFileUsingDocumentFile() {
        try {
            // Tạo Intent để chọn tệp XML
            Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
            intent.setType("text/xml"); // Định dạng MIME phù hợp
            intent.addCategory(Intent.CATEGORY_OPENABLE);

            // Không sử dụng Intent.EXTRA_INITIAL_INTENTS
            // Nếu muốn chọn thư mục cụ thể, cần sử dụng Storage Access Framework (SAF)

            startActivityForResult(intent, 1); // Mã yêu cầu
        } catch (Exception e) {
            Log.e("FileError", "Lỗi khi mở tệp: " + e.getMessage());
            Toast.makeText(this, "Đã xảy ra lỗi khi mở tệp!", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1 && resultCode == RESULT_OK && data != null) {
            Uri fileUri = data.getData(); // URI của tệp được chọn

            if (fileUri != null) {
                Log.d("FileUri", "URI file: " + fileUri.toString());

                // Đọc nội dung XML từ URI
                try (InputStream inputStream = getContentResolver().openInputStream(fileUri)) {
                    if (inputStream != null) {
                        parseXmlFile(inputStream);
                    } else {
                        Log.e("FileError", "Không thể mở luồng dữ liệu từ URI");
                        Toast.makeText(this, "Không thể đọc tệp!", Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    Log.e("XMLParseError", "Lỗi khi đọc tệp XML: " + e.getMessage());
                    Toast.makeText(this, "Lỗi khi phân tích tệp XML!", Toast.LENGTH_SHORT).show();
                }
            } else {
                Log.e("FileError", "URI file null");
                Toast.makeText(this, "Không chọn tệp!", Toast.LENGTH_SHORT).show();
            }
        } else {
            Log.e("FileError", "Không chọn tệp hoặc lỗi xảy ra");
            Toast.makeText(this, "Không chọn tệp hoặc lỗi xảy ra!", Toast.LENGTH_SHORT).show();
        }
    }

    // Phương thức phân tích XML
    private void parseXmlFile(InputStream inputStream) {
        try {
            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            XmlPullParser parser = factory.newPullParser();
            parser.setInput(inputStream, "UTF-8");

            int eventType = parser.getEventType();
            while (eventType != XmlPullParser.END_DOCUMENT) {
                if (eventType == XmlPullParser.START_TAG) {
                    String tagName = parser.getName();
                    if ("customer".equals(tagName)) {
                        String phoneNumber = parser.getAttributeValue(null, "phoneNumber");
                        String currentPoint = parser.getAttributeValue(null, "currentPoint");
                        String creationDate = parser.getAttributeValue(null, "creationDate");
                        String lastUpdatedDate = parser.getAttributeValue(null, "lastUpdatedDate");
                        String note = parser.getAttributeValue(null, "note");

                        Log.d("CustomerXML", "Phone: " + phoneNumber +
                                ", Point: " + currentPoint +
                                ", Created: " + creationDate +
                                ", Updated: " + lastUpdatedDate +
                                ", Note: " + note);
                    }
                }
                eventType = parser.next();
            }
        } catch (Exception e) {
            Log.e("XMLParseError", "Lỗi khi phân tích XML: " + e.getMessage());
        }
    }



    private void sendEmailWithAttachment(File file) {
        if (file.exists()) {
            Uri uri = FileProvider.getUriForFile(this, getApplicationContext().getPackageName() + ".provider", file);

            Intent emailIntent = new Intent(Intent.ACTION_SEND);
            emailIntent.setType("vnd.android.cursor.dir/email");
            emailIntent.putExtra(Intent.EXTRA_SUBJECT, "File XML Danh Sách Khách Hàng");
            emailIntent.putExtra(Intent.EXTRA_TEXT, "Đính kèm là file XML danh sách khách hàng.");
            emailIntent.putExtra(Intent.EXTRA_STREAM, uri);
            emailIntent.setType("message/rfc822");

            try {
                startActivity(Intent.createChooser(emailIntent, "Gửi email với file đính kèm"));
            } catch (ActivityNotFoundException e) {
                Toast.makeText(this, "Không có ứng dụng email nào được cài đặt.", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "File không tồn tại!", Toast.LENGTH_SHORT).show();
        }
    }

}
