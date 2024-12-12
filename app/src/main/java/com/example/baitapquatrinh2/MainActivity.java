package com.example.baitapquatrinh2;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.LayoutInflater;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.baitapquatrinh2.Adapter.CustomerAdapter;
import com.example.baitapquatrinh2.ContentProvider.CustomerProvider;
import com.example.baitapquatrinh2.Credentials.ForgetPasswordActivity;
import com.example.baitapquatrinh2.Customer.InputPointFragment;
import com.example.baitapquatrinh2.Customer.UsePointFragment;
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
        Button btnExport = findViewById(R.id.btnExport);
        btnChangePass = findViewById(R.id.btnChangePass);
        replaceFragment(new WellcomeFragment());
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
        replaceFragment(new InputPointFragment());

            }
        });

        // Button to load use points layout
        buttonUse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                replaceFragment(new UsePointFragment());

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
        customerList = new ArrayList<>(CustomerProvider.loadCustomers(this));

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
    public void replaceFragment(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frameId,fragment);
        transaction.commit();
    }
}
