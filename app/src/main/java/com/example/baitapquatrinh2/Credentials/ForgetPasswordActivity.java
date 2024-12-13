package com.example.baitapquatrinh2.Credentials;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.baitapquatrinh2.ContentProvider.AccountProvider;
import com.example.baitapquatrinh2.R;
import com.example.baitapquatrinh2.Models.Account;

import java.util.List;

public class ForgetPasswordActivity extends AppCompatActivity {
    TextView btnBackLogin;
    Button btnContinue;
    EditText edtUsername;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.forget_password_activity);

        btnBackLogin = findViewById(R.id.btnBackLogin);
        btnContinue = findViewById(R.id.btnOk);
        edtUsername = findViewById(R.id.username);

        btnBackLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ForgetPasswordActivity.this, LoginForm.class);
                startActivity(intent);
                finish();
            }
        });

        // Nút xác nhận
        btnContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = edtUsername.getText().toString();

                if (username.isEmpty()) {
                    Toast.makeText(ForgetPasswordActivity.this, "Please enter a username", Toast.LENGTH_SHORT).show();
                    return;
                }
                List<Account> accountList = AccountProvider.loadAccounts(ForgetPasswordActivity.this);

                // Kiểm tra xem tài khoản có tồn tại không
                boolean usernameExists = false;
                for (Account account : accountList) {
                    if (account.getUsername().equals(username)) {
                        usernameExists = true;
                        break;
                    }
                }

                if (usernameExists) {
                    Intent intent = new Intent(ForgetPasswordActivity.this, FormConfrmChangePass.class);
                    intent.putExtra("username", username);
                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(ForgetPasswordActivity.this, "Username not found", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
