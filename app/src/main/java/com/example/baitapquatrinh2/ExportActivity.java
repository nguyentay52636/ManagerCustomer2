package com.example.baitapquatrinh2;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
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

        // Xử lý sự kiện xuất file
//        btnExportFile.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                List<Customer> customers = getSampleCustomers();
//                xmlFile = XMLHelper.exportCustomersToXML(customers, TestActivity.this);
//                if (xmlFile != null && xmlFile.exists()) {
//                    tvStatus.setText("Xuất file thành công tại " + xmlFile.getPath());
//                    Toast.makeText(TestActivity.this, "Xuất file XML thành công!", Toast.LENGTH_SHORT).show();
//                } else {
//                    tvStatus.setText("Xuất file thất bại.");
//                    Toast.makeText(TestActivity.this, "Xuất file XML thất bại!", Toast.LENGTH_SHORT).show();
//                }
//            }
//        });
//        btnExportFile.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                List<Customer> customers = getSampleCustomers();
//                xmlFile = XMLHelper.exportCustomersToXML(customers, ExportActivity.this);
//
//                if (xmlFile != null && xmlFile.exists()) {
//                    tvStatus.setText("Xuất file thành công tại " + xmlFile.getPath());
//                    Toast.makeText(ExportActivity.this, "Xuất file XML thành công!", Toast.LENGTH_SHORT).show();
//
//                    // Đọc và ghi nội dung file XML ra Logcat
//                    try (BufferedReader reader = new BufferedReader(new FileReader(xmlFile))) {
//                        StringBuilder fileContent = new StringBuilder();
//                        String line;
//                        while ((line = reader.readLine()) != null) {
//                            fileContent.append(line).append("\n");
//                        }
//                        // Ghi nội dung file XML vào Logcat
//                        Log.d("XMLFileContent", fileContent.toString());
//                    } catch (IOException e) {
//                        Log.e("XMLFileContent", "Lỗi khi đọc file XML: " + e.getMessage());
//                    }
//                } else {
//                    tvStatus.setText("Xuất file thất bại.");
//                    Toast.makeText(ExportActivity.this, "Xuất file XML thất bại!", Toast.LENGTH_SHORT).show();
//                }
//            }
//        });
        btnExportFile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<Customer> customers = CustomerProvider.loadCustomers(getApplicationContext());
                File xmlFile = XMLHelper.exportCustomersToXML(customers, ExportActivity.this);

                if (xmlFile != null && xmlFile.exists()) {
                    tvStatus.setText("Xuất file thành công tại " + xmlFile.getPath());
                    Toast.makeText(ExportActivity.this, "Xuất file XML thành công!", Toast.LENGTH_SHORT).show();

                    // Đọc và ghi nội dung file XML ra Logcat
                    try (BufferedReader reader = new BufferedReader(new FileReader(xmlFile))) {
                        StringBuilder fileContent = new StringBuilder();
                        String line;
                        while ((line = reader.readLine()) != null) {
                            fileContent.append(line).append("\n");
                        }
                        Log.d("XMLFileContent", fileContent.toString());  // Log nội dung XML

                    } catch (IOException e) {
                        Log.e("XMLFileContent", "Lỗi khi đọc file XML: " + e.getMessage());
                    }
                } else {
                    tvStatus.setText("Xuất file thất bại.");
                    Toast.makeText(ExportActivity.this, "Xuất file XML thất bại!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        // Xử lý sự kiện mở file
        btnOpenFile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (xmlFile != null && xmlFile.exists()) {
                    openXmlFile(xmlFile);
                } else {
                    Toast.makeText(ExportActivity.this, "File không tồn tại!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        // Xử lý sự kiện gửi email
        btnSendEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (xmlFile != null && xmlFile.exists()) {
                    sendEmailWithAttachment(xmlFile);
                } else {
                    Toast.makeText(ExportActivity.this, "File không tồn tại để gửi email!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private List<Customer> getSampleCustomers() {
        List<Customer> customers = new ArrayList<>();
        customers.add(new Customer("0969157557", 9, "2024-11-10", "2024-11-10", "First customer"));
        customers.add(new Customer("0988888888", 4, "2024-11-10", "2024-11-10", "Second customer"));
        return customers;
    }

    private void openXmlFile(File file) {
        Uri uri = FileProvider.getUriForFile(this, getApplicationContext().getPackageName() + ".provider", file);
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setDataAndType(uri, "text/xml");
        intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        startActivity(Intent.createChooser(intent, "Mở file XML với"));
    }

    private void sendEmailWithAttachment(File file) {
        Uri uri = FileProvider.getUriForFile(this, getApplicationContext().getPackageName() + ".provider", file);
        Intent emailIntent = new Intent(Intent.ACTION_SEND);
        emailIntent.setType("vnd.android.cursor.dir/email");
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "File XML Danh Sách Khách Hàng");
        emailIntent.putExtra(Intent.EXTRA_TEXT, "Đính kèm là file XML danh sách khách hàng.");
        emailIntent.putExtra(Intent.EXTRA_STREAM, uri);
        emailIntent.setType("message/rfc822");
        startActivity(Intent.createChooser(emailIntent, "Gửi email với file đính kèm"));
    }
}
