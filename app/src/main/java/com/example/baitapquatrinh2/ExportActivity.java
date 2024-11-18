package com.example.baitapquatrinh2;

import android.content.ActivityNotFoundException;
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

        btnExportFile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<Customer> customers = CustomerProvider.loadCustomers(getApplicationContext());
                Log.d("CustomerListSize", "Customer list size: " + customers.size());

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

        btnOpenFile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openXmlFile();
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

    private void openXmlFile() {
        // Đường dẫn file
        File file = new File(getExternalFilesDir("Documents"), "customers.xml");
        Log.d("FilePath", "File path: " + file.getAbsolutePath());

        // Kiểm tra xem file có tồn tại không
        if (file.exists()) {
            try {
                // Lấy URI thông qua FileProvider
                Uri uri = FileProvider.getUriForFile(this, getPackageName() + ".provider", file);
                Log.d("FileUri", "URI: " + uri.toString());

                // Tạo intent để mở file
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setDataAndType(uri, "application/xml"); // Thay đổi MIME type chính xác
                intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

                // Kiểm tra xem có ứng dụng nào có thể xử lý intent không
                if (intent.resolveActivity(getPackageManager()) != null) {
                    startActivity(Intent.createChooser(intent, "Mở file XML"));
                } else {
                    Toast.makeText(this, "Không có ứng dụng nào có thể mở file XML", Toast.LENGTH_SHORT).show();
                }
            } catch (IllegalArgumentException e) {
                Log.e("FileProviderError", "Lỗi FileProvider: " + e.getMessage());
                Toast.makeText(this, "Lỗi khi xử lý file với FileProvider", Toast.LENGTH_SHORT).show();
            }
        } else {
            // Thông báo nếu file không tồn tại
            Toast.makeText(this, "File không tồn tại!", Toast.LENGTH_SHORT).show();
            Log.e("FileError", "File không tồn tại tại: " + file.getAbsolutePath());
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
