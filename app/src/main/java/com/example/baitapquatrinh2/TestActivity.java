package com.example.baitapquatrinh2;

import android.os.Bundle;
import android.util.Log;
import androidx.appcompat.app.AppCompatActivity;
import com.example.baitapquatrinh2.ServicesData.DataAccount;
import com.example.baitapquatrinh2.DTO.Account;

import java.util.List;

public class TestActivity extends AppCompatActivity {

    private static final String TAG = "TestActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test);

        // Kiểm tra kết nối cơ sở dữ liệu
        Log.d(TAG, "Đã gọi ConnectDB.connect() để kiểm tra kết nối cơ sở dữ liệu.");

        // Kiểm tra dữ liệu từ DataAccount
        List<Account> accounts = DataAccount.loadAccounts(this);
        if (accounts != null && !accounts.isEmpty()) {
            for (Account account : accounts) {
                Log.d(TAG, "Account: " + account.getUsername() + ", Password: " + account.getPassword());
            }
        } else {
            Log.d(TAG, "Không có dữ liệu tài khoản trong danh sách.");
        }
    }
}
