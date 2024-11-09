package com.example.baitapquatrinh2;

import android.os.Bundle;
import android.util.Log;
import androidx.appcompat.app.AppCompatActivity;
import com.example.baitapquatrinh2.Data.DataAccount;
import com.example.baitapquatrinh2.models.Account;

import java.util.List;

public class TestActivity extends AppCompatActivity {

    private static final String TAG = "TestActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test);

        // Load dữ liệu tài khoản từ file JSON
        DataAccount.loadFromJsonFile(this);

        // Kiểm tra và in ra danh sách tài khoản đã được đọc
        List<Account> accountList = DataAccount.getAccountList();
        if (accountList != null && !accountList.isEmpty()) {
            // Log tất cả tài khoản
            Log.i(TAG, "List of Accounts:");
            for (Account account : accountList) {
                // In ra thông tin tài khoản trong mảng
                Log.i(TAG, "Account: " + account.getUsername() + ", Password: " + account.getPassword());
            }
        } else {
            Log.e(TAG, "No accounts found.");
        }
    }
}
