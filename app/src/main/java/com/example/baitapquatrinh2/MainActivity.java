package com.example.baitapquatrinh2;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.LayoutInflater;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.baitapquatrinh2.Adapter.CustomerAdapter;
import com.example.baitapquatrinh2.Credentials.ForgetPasswordActivity;
import com.example.baitapquatrinh2.LoadData.CustomerData;
import com.example.baitapquatrinh2.Models.Customer;

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
        btnChangePass = findViewById(R.id.btnChangePass);

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
                setupRecyclerView(); // Call setup after layout is loaded
            }
        });
    }

    private void setupRecyclerView() {
        // Load customer data from assets (if not already loaded)
        customerList = new ArrayList<>(CustomerData.loadCustomers(MainActivity.this));

        // Ensure RecyclerView is available in the layout
        recyclerView = container.findViewById(R.id.recyclerView);

        if (recyclerView != null) {
            // Set up RecyclerView
            recyclerView.setLayoutManager(new LinearLayoutManager(this));

            if (customerList != null && !customerList.isEmpty()) {
                // Set up the adapter
                customerAdapter = new CustomerAdapter(customerList);
                recyclerView.setAdapter(customerAdapter);
                Toast.makeText(this, "Danh sách khách hàng đã được tải", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Không có dữ liệu khách hàng", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "Không tìm thấy RecyclerView", Toast.LENGTH_SHORT).show();
        }
    }

    private void loadLayout(int layoutResId) {
        container.removeAllViews();
        LayoutInflater.from(this).inflate(layoutResId, container, true);
    }
}
