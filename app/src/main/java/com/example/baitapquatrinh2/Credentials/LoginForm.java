package com.example.baitapquatrinh2.Credentials;

import static android.content.ContentValues.TAG;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.baitapquatrinh2.Models.Account;
import com.example.baitapquatrinh2.MainActivity;
import com.example.baitapquatrinh2.R;
import com.example.baitapquatrinh2.LoadData.DataAccount;

import java.util.List;

public class LoginForm extends AppCompatActivity {
    EditText userName;
    EditText password;
    Button btnLogin;
    TextView btnForgetPass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        userName = findViewById(R.id.username);
        password = findViewById(R.id.password);
        btnLogin = findViewById(R.id.loginButton);
        btnForgetPass = findViewById(R.id.btnForgetPassword);

        // Xử lý sự kiện quên mật khẩu
        btnForgetPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginForm.this, ForgetPasswordActivity.class);
                startActivity(intent);
            }
        });

        // Xử lý sự kiện đăng nhập
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String usernameInput = userName.getText().toString().trim();
                String passwordInput = password.getText().toString().trim();
                if (usernameInput.isEmpty() || passwordInput.isEmpty()) {
                    Toast.makeText(LoginForm.this, "Please enter both username and password.", Toast.LENGTH_SHORT).show();
                    return;
                }
                List<Account> accountList = DataAccount.loadAccounts(LoginForm.this); // Gọi phương thức loadAccounts để lấy dữ liệu tài khoản

                boolean loginSuccess = false;
                boolean usernameFound = false;

                // Lặp qua danh sách tài khoản để kiểm tra đăng nhập
                for (Account account : accountList) {
                    if (account.getUsername().equals(usernameInput)) {
                        usernameFound = true;
                        if (account.getPassword().equals(passwordInput)) {
                            loginSuccess = true;
                            break;
                        } else {
                            Toast.makeText(LoginForm.this, "Incorrect password.", Toast.LENGTH_SHORT).show();
                            break;
                        }
                    }
                }

                // Xử lý kết quả đăng nhập
                if (usernameFound) {
                    if (loginSuccess) {
                        Toast.makeText(LoginForm.this, "Login success", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(LoginForm.this, MainActivity.class);
                        startActivity(intent);
                    } else {
                        // Thông báo lỗi nếu mật khẩu sai
                        Toast.makeText(LoginForm.this, "Incorrect password.", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    // Thông báo nếu tên đăng nhập không tìm thấy
                    Toast.makeText(LoginForm.this, "Username not found.", Toast.LENGTH_SHORT).show();
                }
            }
        });
//        btnLogin.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                String usernameInput = userName.getText().toString().trim();
//                String passwordInput = password.getText().toString().trim();
//
//                if (usernameInput.isEmpty() || passwordInput.isEmpty()) {
//                    Toast.makeText(LoginForm.this, "Please enter both username and password.", Toast.LENGTH_SHORT).show();
//                    return;
//                }
//
//                // Sử dụng ContentResolver để truy vấn tài khoản từ AccountProvider
//                Uri uri = Uri.parse("content://com.example.baitapquatrinh2.provider/accounts");
//                Cursor cursor = getContentResolver().query(uri, new String[]{"username", "password"}, null, null, null);
//
//                if (cursor != null) {
//                    boolean loginSuccess = false;
//                    boolean usernameFound = false;
//
//                    // Kiểm tra nếu cursor có dữ liệu
//                    while (cursor.moveToNext()) {
//                        // Lấy chỉ số của các cột
//                        int usernameIndex = cursor.getColumnIndex("username");
//                        int passwordIndex = cursor.getColumnIndex("password");
//
//                        // Kiểm tra nếu các chỉ số cột hợp lệ (>= 0)
//                        if (usernameIndex >= 0 && passwordIndex >= 0) {
//                            String username = cursor.getString(usernameIndex);
//                            String password = cursor.getString(passwordIndex);
//
//                            if (username.equals(usernameInput)) {
//                                usernameFound = true;
//                                if (password.equals(passwordInput)) {
//                                    loginSuccess = true;
//                                    break;
//                                } else {
//                                    Toast.makeText(LoginForm.this, "Incorrect password.", Toast.LENGTH_SHORT).show();
//                                    break;
//                                }
//                            }
//                        } else {
////                            Log.e(AG, "Cột không tồn tại trong cursor");
//                        }
//                    }
//
//                    // Xử lý kết quả đăng nhập
//                    if (usernameFound) {
//                        if (loginSuccess) {
//                            Toast.makeText(LoginForm.this, "Login success", Toast.LENGTH_SHORT).show();
//                            Intent intent = new Intent(LoginForm.this, MainActivity.class);
//                            startActivity(intent);
//                        } else {
//                            Toast.makeText(LoginForm.this, "Incorrect password.", Toast.LENGTH_SHORT).show();
//                        }
//                    } else {Toast.makeText(LoginForm.this, "Username not found.", Toast.LENGTH_SHORT).show();
//                    }
//
//                    // Đóng cursor sau khi sử dụng xong
//                    cursor.close();
//                }
//            }
//        });



    }
}
