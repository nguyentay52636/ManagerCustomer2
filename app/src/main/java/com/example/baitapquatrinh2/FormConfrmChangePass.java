package com.example.baitapquatrinh2;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.baitapquatrinh2.Data.DataAccount;
import com.example.baitapquatrinh2.models.Account;

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
                        boolean isUpdated = resetPassword(username, newPassword);
                        if (isUpdated) {
                            saveToPreferences();
                            Toast.makeText(FormConfrmChangePass.this, "Password updated successfully!", Toast.LENGTH_SHORT).show();
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

    private Boolean resetPassword(String username, String newPassword) {
        return DataAccount.updatePassword(username, newPassword, this);
    }

    private void saveToPreferences() {
        SharedPreferences sharedPreferences = getSharedPreferences("accounts", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();

        for (Account account : DataAccount.getAccountList()) {
            editor.putString(account.getUsername(), account.getPassword());
        }

        editor.apply();
    }
}
