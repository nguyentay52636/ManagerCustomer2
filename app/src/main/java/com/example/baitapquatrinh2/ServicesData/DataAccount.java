package com.example.baitapquatrinh2.ServicesData;

import com.example.baitapquatrinh2.DTO.Account;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import android.content.Context;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class DataAccount {

    private static List<Account> accountList = new ArrayList<>();

    // Phương thức đọc file JSON từ thư mục assets
    public static List<Account> loadAccounts(Context context) {
        try {
            // Mở file từ thư mục assets mà không cần đường dẫn tuyệt đối
            InputStream inputStream = context.getAssets().open("accounts.json");
            InputStreamReader reader = new InputStreamReader(inputStream);
            accountList = new Gson().fromJson(reader, new TypeToken<List<Account>>() {}.getType());
            reader.close();
            inputStream.close();
        } catch (IOException e) {
            Log.e("DataAccount", "Lỗi khi đọc file accounts.json: " + e.getMessage());
        }
        return accountList;
    }

    public static void updatePassword(String username, String newPassword, Context context) {
        for (Account account : accountList) {
            if (account.getUsername().equals(username)) {
                account.setPassword(newPassword);
                saveAccounts(context);
                break;
            }
        }
    }
    public static List<Account> getAccountList() {
        return accountList;
    }

    public static void saveAccounts(Context context) {  // Thay 'private' thành 'public'
        try {
            String json = new Gson().toJson(accountList);
            // Lưu dữ liệu vào file trong bộ nhớ của ứng dụng, không sử dụng đường dẫn tuyệt đối
            context.openFileOutput("accounts.json", Context.MODE_PRIVATE).write(json.getBytes());
        } catch (IOException e) {
            Log.e("DataAccount", "Lỗi khi ghi file accounts.json: " + e.getMessage());
        }
    }


}
