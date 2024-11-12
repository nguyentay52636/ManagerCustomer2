package com.example.baitapquatrinh2.Customer;

import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.LinearLayoutManager;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import com.example.baitapquatrinh2.Adapter.CustomerAdapter;
import com.example.baitapquatrinh2.ContentProvider.CustomerProvider;
import com.example.baitapquatrinh2.R;
import com.example.baitapquatrinh2.LoadData.CustomerData;
import com.example.baitapquatrinh2.Models.Customer;
import java.util.ArrayList;
import android.widget.Toast;

public class ListCustomersActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private CustomerAdapter customerAdapter;
    private ArrayList<Customer> customerList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_item_customer);  // Make sure your layout file exists

        // Khởi tạo RecyclerView
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Load customer data from JSON file in assets
        customerList = new ArrayList<>(CustomerProvider.loadCustomers(this));  // Initialize if null

        // Check if data is loaded correctly
        if (customerList.isEmpty()) {
            Toast.makeText(this, "No customers found", Toast.LENGTH_SHORT).show();
        }

        // Set up adapter
        customerAdapter = new CustomerAdapter(customerList);
        recyclerView.setAdapter(customerAdapter);
    }
}
