package com.example.baitapquatrinh2.Utils;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.widget.FrameLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.baitapquatrinh2.Adapter.CustomerAdapter;
import com.example.baitapquatrinh2.MainActivity;
import com.example.baitapquatrinh2.Models.Customer;
import com.example.baitapquatrinh2.R;

import java.util.ArrayList;

public class TestActivity extends AppCompatActivity {
//    private ArrayList<Customer> customerList;
//    private RecyclerView recyclerView;
//    private CustomerAdapter customerAdapter;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.list_item_customer);
//
//        // Tìm RecyclerView
//        recyclerView = findViewById(R.id.recyclerView);
//
//        // Kiểm tra nếu RecyclerView tồn tại
//        if (recyclerView == null) {
//            Toast.makeText(this, "Không tìm thấy RecyclerView", Toast.LENGTH_SHORT).show();
//            return;
//        }
//
//        setupRecyclerView();
//    }
//
//    private void setupRecyclerView() {
//        // Tải dữ liệu khách hàng
//        customerList = new ArrayList<>(CustomerData.loadCustomers(this));
//
//        // Kiểm tra nếu danh sách khách hàng trống
//        if (customerList == null || customerList.isEmpty()) {
//            Toast.makeText(this, "Không có dữ liệu khách hàng", Toast.LENGTH_SHORT).show();
//            return;
//        }
//
//        // Thiết lập LayoutManager cho RecyclerView
//        recyclerView.setLayoutManager(new LinearLayoutManager(this));
//
//        // Thiết lập Adapter
//        customerAdapter = new CustomerAdapter(customerList);
//        recyclerView.setAdapter(customerAdapter);
//        Toast.makeText(this, "Danh sách khách hàng đã được tải", Toast.LENGTH_SHORT).show();
//    }
}

