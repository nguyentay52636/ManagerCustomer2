package com.example.baitapquatrinh2.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.baitapquatrinh2.R;
import com.example.baitapquatrinh2.DTO.Customer;
import java.util.ArrayList;

public class CustomerAdapter extends RecyclerView.Adapter<CustomerAdapter.CustomerViewHolder> {
    private ArrayList<Customer> customerList;

    public CustomerAdapter(ArrayList<Customer> customerList) {
        this.customerList = customerList;
    }

    @NonNull
    @Override
    public CustomerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_customer, parent, false);
        return new CustomerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CustomerViewHolder holder, int position) {
        Customer customer = customerList.get(position);
        holder.tvPhoneNumber.setText(customer.getPhoneNumber());
        holder.tvCreationDate.setText(customer.getCreationDate());
        holder.tvLastUpdatedDate.setText(customer.getLastUpdatedDate());
        holder.tvCurrentPoint.setText(String.valueOf(customer.getCurrentPoint()));
        holder.tvNote.setText(customer.getNote());
    }

    @Override
    public int getItemCount() {
        return customerList.size();
    }

    public static class CustomerViewHolder extends RecyclerView.ViewHolder {
        TextView tvPhoneNumber, tvCreationDate, tvLastUpdatedDate, tvCurrentPoint, tvNote;

        public CustomerViewHolder(@NonNull View itemView) {
            super(itemView);
            tvPhoneNumber = itemView.findViewById(R.id.tvPhoneNumber);
            tvCreationDate = itemView.findViewById(R.id.tvCreationDate);
            tvLastUpdatedDate = itemView.findViewById(R.id.tvLastUpdatedDate);
            tvCurrentPoint = itemView.findViewById(R.id.tvCurrentPoint);
            tvNote =  itemView.findViewById(R.id.tvNote);
        }
    }
}


