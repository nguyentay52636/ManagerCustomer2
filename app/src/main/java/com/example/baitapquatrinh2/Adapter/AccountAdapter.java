package com.example.baitapquatrinh2.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.baitapquatrinh2.Models.Account;
import com.example.baitapquatrinh2.R;

import java.util.ArrayList;

public class AccountAdapter extends RecyclerView.Adapter<AccountAdapter.AccountViewHolder> {

    private ArrayList<Account> accountList;

    public AccountAdapter(ArrayList<Account> accountList) {
        this.accountList = accountList;
    }

    @Override
    public AccountViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // Inflate layout cho mỗi item trong RecyclerView
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_account, parent, false);
        return new AccountViewHolder(view);
    }

    @Override
    public void onBindViewHolder(AccountViewHolder holder, int position) {
        // Gán dữ liệu cho từng item
        Account account = accountList.get(position);
        holder.usernameTextView.setText(account.getUsername());
        holder.passwordTextView.setText(account.getPassword());
    }

    @Override
    public int getItemCount() {
        return accountList.size();
    }

    // ViewHolder để chứa các thành phần giao diện của mỗi item
    public static class AccountViewHolder extends RecyclerView.ViewHolder {
        TextView usernameTextView;
        TextView passwordTextView;

        public AccountViewHolder(View itemView) {
            super(itemView);
            usernameTextView = itemView.findViewById(R.id.username);
            passwordTextView = itemView.findViewById(R.id.password);
        }
    }
}
