package com.example.baitapquatrinh2.Data;

import android.content.Context;
import android.util.Log;

import com.example.baitapquatrinh2.models.Account;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class DataAccount {

    private static List<Account> accountList = new ArrayList<>();
    private static final String FILE_NAME = "accounts.json";
    private static final String TAG = "DataAccount";

    // Lấy danh sách tài khoản
    public static List<Account> getAccountList() {
        return accountList;
    }

    // Cập nhật mật khẩu và lưu vào file JSON
    public static boolean updatePassword(String username, String newPassword, Context context) {
        for (Account account : accountList) {
            if (account.getUsername().equals(username)) {
                account.setPassword(newPassword);
                return saveToJsonFile(context);
            }
        }
        return false;
    }

    public static boolean saveToJsonFile(Context context) {
        Gson gson = new Gson();
        File file = new File(context.getFilesDir(), FILE_NAME);

        try (FileWriter writer = new FileWriter(file)) {
            gson.toJson(accountList, writer);
            Log.i(TAG, "Data saved to JSON file: " + file.getAbsolutePath());
            return true;
        } catch (IOException e) {
            Log.e(TAG, "Error saving to JSON: " + e.getMessage());
            return false;
        }
    }

    // Đọc dữ liệu tài khoản từ file JSON và chuyển thành ArrayList<Account>
    public static void loadFromJsonFile(Context context) {
        Gson gson = new Gson();
        File file = new File(context.getFilesDir(), FILE_NAME);

        if (!file.exists()) {
            Log.e(TAG, "File does not exist. Creating sample data.");
            createSampleData(context);  // Nếu tệp không tồn tại, tạo dữ liệu mẫu
            return;
        }

        try (FileReader reader = new FileReader(file)) {
            Type listType = new TypeToken<ArrayList<Account>>(){}.getType();
            accountList = gson.fromJson(reader, listType);

            if (accountList == null || accountList.isEmpty()) {
                Log.e(TAG, "No accounts found in JSON.");
                createSampleData(context);  // Nếu không có dữ liệu, tạo dữ liệu mẫu
            } else {
                Log.i(TAG, "Loaded " + accountList.size() + " accounts from JSON.");
            }
        } catch (IOException e) {
            Log.e(TAG, "Error reading file: " + e.getMessage());
        }
    }

    // Tạo dữ liệu mẫu nếu file không tồn tại hoặc bị trống
    private static void createSampleData(Context context) {
        List<Account> sampleAccounts = new ArrayList<>();
        sampleAccounts.add(new Account("user1", "password123"));
        sampleAccounts.add(new Account("user2", "password2"));
        sampleAccounts.add(new Account("user3", "password3"));
        sampleAccounts.add(new Account("user4", "password4"));
        sampleAccounts.add(new Account("user5", "password5"));

        accountList = sampleAccounts;  // Gán dữ liệu mẫu vào list
        saveToJsonFile(context);  // Lưu dữ liệu mẫu vào tệp JSON
    }
}
