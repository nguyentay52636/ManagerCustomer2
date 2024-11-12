package com.example.baitapquatrinh2;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.LayoutInflater;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.baitapquatrinh2.Adapter.CustomerAdapter;
import com.example.baitapquatrinh2.Credentials.ForgetPasswordActivity;
import com.example.baitapquatrinh2.LoadData.CustomerData;
import com.example.baitapquatrinh2.Models.Customer;

import java.io.File;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private FrameLayout container;
    private RecyclerView recyclerView;
    private CustomerAdapter customerAdapter;
    private ArrayList<Customer> customerList;
    Button btnChangePass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_layout_activity);

        container = findViewById(R.id.frameId);
        Button buttonInput = findViewById(R.id.btnInput);
        Button buttonUse = findViewById(R.id.btnUse);
        Button buttonList = findViewById(R.id.btnList);
        Button btnExport = findViewById(R.id.btnExport);
        btnChangePass = findViewById(R.id.btnChangePass);
//        btnExport.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                List<Customer> customers = CustomerProvider.loadCustomersFromJSON(MainActivity.this);
//
//                if (customers != null) {
//                    XMLHelper.exportCustomersToXML(customers, MainActivity.this);
//                    Toast.makeText(MainActivity.this, "Xuất dữ liệu thành công!", Toast.LENGTH_SHORT).show();
//                } else {
//                    Toast.makeText(MainActivity.this, "Không có dữ liệu để xuất", Toast.LENGTH_SHORT).show();
//                }
//            }
//        });
//
        btnExport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ExportActivity.class);
                startActivity(intent);
            }
        });



        // Handle change password button
        btnChangePass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ForgetPasswordActivity.class);
                startActivity(intent);
            }
        });

        // Button to load input points layout
        buttonInput.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadLayout(R.layout.input_point_activity);
            }
        });

        // Button to load use points layout
        buttonUse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadLayout(R.layout.use_point_activity);
            }
        });

        buttonList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadLayout(R.layout.list_item_customer);
                recyclerView = findViewById(R.id.recyclerView); // Tìm lại RecyclerView sau khi layout mới được load
                if (recyclerView == null) {
                    return;
                }
                setupRecyclerView();

            }
        });
    }
    private void loadLayout(int layoutResId) {
        container.removeAllViews();
        LayoutInflater.from(this).inflate(layoutResId, container, true);
    }
    private void setupRecyclerView() {
        // Tải dữ liệu khách hàng
        customerList = new ArrayList<>(CustomerData.loadCustomers(this));

        // Kiểm tra nếu danh sách khách hàng trống
        if (customerList == null || customerList.isEmpty()) {
            Toast.makeText(this, "Không có dữ liệu khách hàng", Toast.LENGTH_SHORT).show();
            return;
        }

        // Thiết lập LayoutManager cho RecyclerView
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Thiết lập Adapter
        customerAdapter = new CustomerAdapter(customerList);
        recyclerView.setAdapter(customerAdapter);
        Toast.makeText(this, "Danh sách khách hàng đã được tải", Toast.LENGTH_SHORT).show();
    }


    private void openExportedFile(File file) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setDataAndType(Uri.fromFile(file), "text/xml");
        intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);

        // Kiểm tra nếu có ứng dụng hỗ trợ mở file XML
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        } else {
            Toast.makeText(this, "Không có ứng dụng hỗ trợ mở file", Toast.LENGTH_SHORT).show();
        }
    }
    private void sendEmailWithAttachment(File filePath) {
        Uri fileUri = FileProvider.getUriForFile(
                MainActivity.this,
                getApplicationContext().getPackageName() + ".provider",
                filePath);


        Intent emailIntent = new Intent(Intent.ACTION_SEND);
        emailIntent.setType("message/rfc822");
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Danh sách khách hàng");
        emailIntent.putExtra(Intent.EXTRA_TEXT, "Đây là file XML danh sách khách hàng.");
        emailIntent.putExtra(Intent.EXTRA_STREAM, fileUri);
        emailIntent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

        try {
            startActivity(Intent.createChooser(emailIntent, "Chọn ứng dụng Email"));
        } catch (Exception e) {
            Toast.makeText(MainActivity.this, "Không có ứng dụng Email nào được cài đặt", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

}
