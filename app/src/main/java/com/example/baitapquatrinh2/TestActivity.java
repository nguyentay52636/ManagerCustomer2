package com.example.baitapquatrinh2;

import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import androidx.appcompat.app.AppCompatActivity;
import com.example.baitapquatrinh2.LoadData.DataAccount;
import com.example.baitapquatrinh2.Models.Account;

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

        // Thêm tài khoản mới vào ContentProvider
        ContentValues values = new ContentValues();
        values.put("username", "newUser");
        values.put("password", "newPassword");

        Uri uri = getContentResolver().insert(Uri.parse("content://com.example.baitapquatrinh2.provider/accounts"), values);
        if (uri != null) {
            Log.d(TAG, "Tài khoản đã được thêm: " + uri.toString());
        } else {
            Log.d(TAG, "Lỗi khi thêm tài khoản.");
        }

        // Truy vấn tất cả tài khoản từ ContentProvider
        Cursor cursor = getContentResolver().query(
                Uri.parse("content://com.example.baitapquatrinh2.provider/accounts"),
                null, null, null, null
        );

        if (cursor != null && cursor.moveToFirst()) {
            do {
                String username = cursor.getString(cursor.getColumnIndex("username"));
                String password = cursor.getString(cursor.getColumnIndex("password"));
                Log.d(TAG, "Account: " + username + ", Password: " + password);
            } while (cursor.moveToNext());
            cursor.close();
        } else {
            Log.d(TAG, "Không có tài khoản nào trong ContentProvider.");
        }

        // Cập nhật mật khẩu cho tài khoản "newUser"
        ContentValues updateValues = new ContentValues();
        updateValues.put("password", "newUpdatedPassword");

        int rowsUpdated = getContentResolver().update(
                Uri.parse("content://com.example.baitapquatrinh2.provider/accounts"),
                updateValues,
                "username=?",
                new String[]{"newUser"}
        );

        if (rowsUpdated > 0) {
            Log.d(TAG, "Cập nhật mật khẩu thành công.");
        } else {
            Log.d(TAG, "Không thể cập nhật mật khẩu.");
        }
    }
}
