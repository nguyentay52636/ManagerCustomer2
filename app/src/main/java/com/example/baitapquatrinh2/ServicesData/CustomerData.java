package com.example.baitapquatrinh2.ServicesData;

import com.example.baitapquatrinh2.DTO.Customer;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import android.content.Context;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class CustomerData {

    private static List<Customer> customerList = new ArrayList<>();

    // Phương thức đọc file JSON từ thư mục assets
    public static List<Customer> loadCustomers(Context context) {
        List<Customer> customerList = new ArrayList<>();  // Initialize here
        try {
            InputStream inputStream = context.getAssets().open("customers.json");
            InputStreamReader reader = new InputStreamReader(inputStream);
            customerList = new Gson().fromJson(reader, new TypeToken<List<Customer>>() {}.getType());
            reader.close();
            inputStream.close();
        } catch (IOException e) {
            Log.e("CustomerData", "Lỗi khi đọc file customers.json: " + e.getMessage());
        }
        return customerList;  // Ensure you return a non-null list
    }


    // Lấy danh sách khách hàng
    public static List<Customer> getCustomerList() {
        return customerList;
    }

    // Lưu danh sách khách hàng vào file
    public static void saveCustomers(Context context) {
        try {
            String json = new Gson().toJson(customerList);
            // Lưu dữ liệu vào file trong bộ nhớ của ứng dụng
            context.openFileOutput("customers.json", Context.MODE_PRIVATE).write(json.getBytes());
        } catch (IOException e) {
            Log.e("CustomerData", "Lỗi khi ghi file customers.json: " + e.getMessage());
        }
    }

    // Phương thức lấy ngày hiện tại dưới định dạng "yyyy-MM-dd"
    private static String getCurrentDate() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        return sdf.format(new Date());
    }
}
