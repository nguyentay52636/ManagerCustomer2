package com.example.baitapquatrinh2;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.LayoutInflater;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.util.Log;  // Thêm log

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.baitapquatrinh2.Adapter.CustomerAdapter;
import com.example.baitapquatrinh2.Data.CustomerData;
import com.example.baitapquatrinh2.Data.DataAccount;
import com.example.baitapquatrinh2.models.Account;
import com.example.baitapquatrinh2.models.Customer;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private FrameLayout container;
    private RecyclerView recyclerView;
    private CustomerAdapter customerAdapter;
    private ArrayList<Customer> customerList;
    Button btnChangePass ;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_layout_activity);


        container = findViewById(R.id.frameId);
        Button buttonInput = findViewById(R.id.btnInput);
        Button buttonUse = findViewById(R.id.btnUse);
        Button buttonList = findViewById(R.id.btnList);
        btnChangePass = findViewById(R.id.btnChangePass) ;



        btnChangePass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,ForgetPasswordActivity.class);
                startActivity(intent);
//                Boolean loginSuccess = false;
//                TextView userName = findViewById(R.id.username) ;
//                TextView password = findViewById(R.id.password);


//                for(Account account : DataAccount.getAccountList()) {
//                    // Kiểm tra nếu username và password khớp
//                    if(account.getUsername().equals(userName.getText().toString()) &&
//                            account.getPassword().equals(password.getText().toString())) {
//                        loginSuccess = true;
//                        break;
//                    }
//                }
//                if(loginSuccess) {
//                    Toast.makeText(MainActivity.this, "Change password  success!", Toast.LENGTH_SHORT).show();
//
//                } else {
//                    Toast.makeText(MainActivity.this, "Change password  failed!", Toast.LENGTH_SHORT).show();
//                }
                }
        });

        buttonInput.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadLayout(R.layout.input_point_activity);
            }
        });

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
                setupRecyclerView();
            }
        });
    }
    private void setupRecyclerView() {
        RecyclerView recyclerView = container.findViewById(R.id.recyclerView);
        if (recyclerView != null) {
            recyclerView.setLayoutManager(new LinearLayoutManager(this));
            customerList = CustomerData.getSampleCustomerList();
            CustomerAdapter customerAdapter = new CustomerAdapter(customerList);
            recyclerView.setAdapter(customerAdapter);
            Toast.makeText(this, "Danh sách khách hàng đã được tải", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Không tìm thấy RecyclerView", Toast.LENGTH_SHORT).show();
        }
    }
    private void loadLayout(int layoutResId) {
        container.removeAllViews();
        LayoutInflater.from(this).inflate(layoutResId, container, true);
    }
}
