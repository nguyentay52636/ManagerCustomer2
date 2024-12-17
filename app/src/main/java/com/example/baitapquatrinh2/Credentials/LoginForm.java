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

import com.example.baitapquatrinh2.ContentProvider.AccountProvider;
import com.example.baitapquatrinh2.Models.Account;
import com.example.baitapquatrinh2.MainActivity;
import com.example.baitapquatrinh2.R;

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
                List<Account> accountList = AccountProvider.loadAccounts(LoginForm.this); // Gọi phương thức loadAccounts để lấy dữ liệu tài khoản

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
    }
}
