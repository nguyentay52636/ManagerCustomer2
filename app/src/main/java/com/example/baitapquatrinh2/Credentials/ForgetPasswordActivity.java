package com.example.baitapquatrinh2.Credentials;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.baitapquatrinh2.R;
import com.example.baitapquatrinh2.LoadData.DataAccount;
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

        // Nút quay lại đăng nhập
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

                // Gọi phương thức loadAccounts để lấy danh sách tài khoản từ file
                List<Account> accountList = DataAccount.loadAccounts(ForgetPasswordActivity.this);

                // Kiểm tra xem tài khoản có tồn tại không
                boolean usernameExists = false;
                for (Account account : accountList) {
                    if (account.getUsername().equals(username)) {
                        usernameExists = true;
                        break;
                    }
                }

                if (usernameExists) {
                    // Nếu username tồn tại, chuyển sang màn hình thay đổi mật khẩu
                    Intent intent = new Intent(ForgetPasswordActivity.this, FormConfrmChangePass.class);
                    intent.putExtra("username", username); // Truyền username sang màn hình thay đổi mật khẩu
                    startActivity(intent);
                    finish();
                } else {
                    // Nếu username không tồn tại, hiển thị thông báo
                    Toast.makeText(ForgetPasswordActivity.this, "Username not found", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
