package com.example.baitapquatrinh2;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.baitapquatrinh2.Data.DataAccount;
import com.example.baitapquatrinh2.models.Account;

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

                // Kiểm tra người dùng có nhập tên đăng nhập và mật khẩu hay không
                if (usernameInput.isEmpty() || passwordInput.isEmpty()) {
                    Toast.makeText(LoginForm.this, "Please enter both username and password.", Toast.LENGTH_SHORT).show();
                    return;
                }

                boolean loginSuccess = false;

                for (Account account : DataAccount.getAccountList()) {
                    if (account.getUsername().equals(usernameInput)) {
                        if (account.getPassword().equals(passwordInput)) {
                            loginSuccess = true;
                        } else {
                            Toast.makeText(LoginForm.this, "Incorrect password.", Toast.LENGTH_SHORT).show();
                        }
                        break;
                    }
                }

                // Xử lý kết quả đăng nhập
                if (loginSuccess) {
                    Toast.makeText(LoginForm.this, "Login success", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(LoginForm.this, MainActivity.class);
                    startActivity(intent);
                } else {
                    Toast.makeText(LoginForm.this, "Login failed. Invalid username.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
