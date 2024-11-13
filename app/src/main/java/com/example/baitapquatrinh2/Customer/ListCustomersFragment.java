package com.example.baitapquatrinh2.Customer;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.LinearLayoutManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.baitapquatrinh2.Adapter.CustomerAdapter;
import com.example.baitapquatrinh2.ContentProvider.CustomerProvider;
import com.example.baitapquatrinh2.Models.Customer;
import com.example.baitapquatrinh2.R;

import java.util.ArrayList;

public class ListCustomersFragment extends Fragment {
    private RecyclerView recyclerView;
    private CustomerAdapter customerAdapter;
    private ArrayList<Customer> customerList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.list_item_customer, container, false);

        // Khởi tạo RecyclerView
        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        // Load customer data from JSON file in assets
        customerList = new ArrayList<>(CustomerProvider.loadCustomers(getContext()));

        // Check if data is loaded correctly
        if (customerList.isEmpty()) {
            Toast.makeText(getContext(), "No customers found", Toast.LENGTH_SHORT).show();
        }

        // Set up adapter
        customerAdapter = new CustomerAdapter(customerList);
        recyclerView.setAdapter(customerAdapter);

        return view;
    }
}
