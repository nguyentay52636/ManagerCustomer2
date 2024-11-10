package com.example.baitapquatrinh2.Credentials;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.baitapquatrinh2.R;
import com.example.baitapquatrinh2.ServicesData.DataAccount;
import com.example.baitapquatrinh2.DTO.Account;

import java.util.List;

public class FormConfrmChangePass extends AppCompatActivity {
    EditText userName, password, rePassword;
    Button btnConfirm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.forget_password_displaychangepass_activity);

        userName = findViewById(R.id.username);
        password = findViewById(R.id.password);
        rePassword = findViewById(R.id.passwordAgain);
        btnConfirm = findViewById(R.id.Confirm);

        Intent intent = getIntent();
        String username = intent.getStringExtra("username");
        if (username != null) {
            userName.setText(username);

            btnConfirm.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String newPassword = password.getText().toString();
                    String confirmPassword = rePassword.getText().toString();

                    if (newPassword.isEmpty() || confirmPassword.isEmpty()) {
                        Toast.makeText(FormConfrmChangePass.this, "Password fields cannot be empty!", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    if (newPassword.equals(confirmPassword)) {
                        // Cập nhật mật khẩu
                        boolean isUpdated = updatePassword(username, newPassword);
                        if (isUpdated) {
                            Toast.makeText(FormConfrmChangePass.this, "Password updated successfully!", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(FormConfrmChangePass.this, LoginForm.class);
                            startActivity(intent);
                            finish();
                        } else {
                            Toast.makeText(FormConfrmChangePass.this, "Error updating password or username not found.", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(FormConfrmChangePass.this, "Passwords do not match!", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }


    private boolean updatePassword(String username, String newPassword) {
        List<Account> accountList = DataAccount.loadAccounts(FormConfrmChangePass.this);
        for (Account account : accountList) {
            if (account.getUsername().equals(username)) {
                account.setPassword(newPassword);

                DataAccount.saveAccounts(FormConfrmChangePass.this);
                return true;  // Trả về true khi thành công
            }
        }
        return false;
    }
    }

